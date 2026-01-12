package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Tag(
    name = "User Controller",
    description = "User 관련 Controller입니다."
)
@RequestMapping("/api/users")
public interface UserApi {

  @Operation(
      summary = "사용자 생성",
      description = "사용자를 생성할 수 있습니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "User가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = User.class))
      ),
      @ApiResponse(
          responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(examples = @ExampleObject(value = "User with email {email} already exists"))
      ),
  })
  ResponseEntity<User> create(
      @Parameter(
          name = "userCreateRequest",
          description = "생성할 사용자 정보",
          required = true,
          schema = @Schema(implementation = UserCreateRequest.class)
      )
      UserCreateRequest userCreateRequest,
      @Parameter(
          name = "profile",
          description = "사용자 프로필 이미지",
          schema = @Schema(implementation = MultipartFile.class)
      )
      MultipartFile profile
  );

  @Operation(
      summary = "사용자 수정",
      description = "사용자를 수정할 수 있습니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "User 정보가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = User.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject("User with id {userId} not found"))
      ),
      @ApiResponse(
          responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(examples = @ExampleObject("user with email {newEmail} already exists"))
      )
  })
  ResponseEntity<User> update(
      @Parameter(
          name = "userId",
          description = "수정할 사용자 ID",
          in = ParameterIn.PATH,
          schema = @Schema(implementation = UUID.class)
      )
      UUID userId,
      @Parameter(
          name = "userUpdateRequest",
          description = "수정할 사용자 입력정보",
          schema = @Schema(implementation = UserUpdateRequest.class)
      )
      UserUpdateRequest userUpdateRequest,
      @Parameter(
          name = "profile",
          description = "수정할 사용자 프로필",
          schema = @Schema(implementation = MultipartFile.class)
      )
      MultipartFile profile
  );

  @Operation(
      summary = "사용자 제거",
      description = "사용자를 제거할 수 있습니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "User가 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "User with id {id} not found"))
      )
  })
  ResponseEntity<Void> delete(
      @Parameter(
          name = "userId",
          description = "삭제할 사용자 ID",
          in = ParameterIn.PATH,
          schema = @Schema(implementation = UUID.class)
      )
      UUID userId
  );

  @Operation(
      summary = "모든 사용자 조회",
      description = "사용자를 조회할 수 있습니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "User 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
      )
  })
  ResponseEntity<List<UserDto>> findAll();

  @Operation(summary = "사용자 상태 수정", description = "사용자의 상태를 수정할 수 있습니다.")
  @ApiResponses(value = {

      @ApiResponse(
          responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨",
          content = @Content(schema = @Schema(implementation = UserStatus.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "UserStatus with userId {userId} not found"))
      )
  })
  ResponseEntity<UserStatus> updateUserStatusByUserId(
      @Parameter(
          name = "userId",
          description = "수정할 사용자 ID",
          in = ParameterIn.PATH,
          schema = @Schema(implementation = UUID.class)
      )
      UUID userId,
      @Parameter(
          name = "request",
          description = "수정할 사용자 입력정보",
          schema = @Schema(implementation = UserStatusUpdateRequest.class)
      )
      UserStatusUpdateRequest request);
}
