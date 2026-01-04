package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "User Controller", description = "User 관련 Controller입니다.")
@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @Operation(summary = "사용자 생성", description = "사용자를 생성할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  @PostMapping(path = "create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<User> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    User createdUser = userService.create(userCreateRequest, profileRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @Operation(summary = "사용자 수정", description = "사용자를 수정할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  @PatchMapping(path = "update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<User> update(@RequestParam("userId") UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    User updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
    return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
  }

  @Operation(summary = "사용자 제거", description = "사용자를 제거할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "제거 성공", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  @DeleteMapping("/delete")
  public ResponseEntity<Void> delete(@RequestParam("userId") UUID userId) {
    userService.delete(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "모든 사용자 조회", description = "사용자를 조회할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  @GetMapping("/findAll")
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @Operation(summary = "사용자 상태 수정", description = "사용자의 상태를 수정할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = UserStatus.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  @PatchMapping("updateUserStatusByUserId")
  public ResponseEntity<UserStatus> updateUserStatusByUserId(@RequestParam("userId") UUID userId,
      @RequestBody UserStatusUpdateRequest request) {
    UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, request);
    return ResponseEntity.status(HttpStatus.OK).body(updatedUserStatus);
  }

  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
            profileFile.getOriginalFilename(), profileFile.getContentType(),
            profileFile.getBytes());
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
