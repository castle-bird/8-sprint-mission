package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User Controller", description = "User 관련 Controller입니다.")
@RequestMapping("/api/users")
public interface UserApi {

  @Operation(summary = "사용자 생성", description = "사용자를 생성할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<User> create(UserCreateRequest userCreateRequest, MultipartFile profile);

  @Operation(summary = "사용자 수정", description = "사용자를 수정할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<User> update(UUID userId, UserUpdateRequest userUpdateRequest,
      MultipartFile profile);

  @Operation(summary = "사용자 제거", description = "사용자를 제거할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "제거 성공", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<Void> delete(UUID userId);

  @Operation(summary = "모든 사용자 조회", description = "사용자를 조회할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<List<UserDto>> findAll();

  @Operation(summary = "사용자 상태 수정", description = "사용자의 상태를 수정할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = UserStatus.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<UserStatus> updateUserStatusByUserId(UUID userId, UserStatusUpdateRequest request);
}
