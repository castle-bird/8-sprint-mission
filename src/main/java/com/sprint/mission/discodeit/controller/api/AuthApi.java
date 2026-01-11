package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth Controller", description = "Login 관련 컨트롤러입니다.")
@RequestMapping("/api/auth")
public interface AuthApi {

  @Operation(summary = "로그인", description = "로그인을 할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "401", description = "인증 실패 (아이디/비밀번호 불일치)", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))})
  ResponseEntity<User> login(LoginRequest loginRequest);
}
