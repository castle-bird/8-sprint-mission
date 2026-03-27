package com.sprint.mission.discodeit.auth.controller;

import com.sprint.mission.discodeit.auth.config.SpaCsrfTokenRequestHandler;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

  private final AuthService authService;
  private final SpaCsrfTokenRequestHandler spaCsrfTokenRequestHandler;

  // csrf 발급
  @GetMapping("/csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
    log.info("csrfToken 발급 = {}", csrfToken);

    return ResponseEntity.ok().build();
  }


  // 로그인
  @PostMapping(path = "login")
  public ResponseEntity<UserDto> login(@RequestBody @Valid LoginRequest loginRequest) {
    log.info("로그인 요청: username={}", loginRequest.username());
    UserDto user = authService.login(loginRequest);
    log.debug("로그인 응답: {}", user);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(user);
  }
}
