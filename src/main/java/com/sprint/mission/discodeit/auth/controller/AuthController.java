package com.sprint.mission.discodeit.auth.controller;

import com.sprint.mission.discodeit.auth.config.SpaCsrfTokenRequestHandler;
import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
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

  private final DiscodeitUserDetailsService discodeitUserDetailsService;
  private final SpaCsrfTokenRequestHandler spaCsrfTokenRequestHandler;

  // csrf 발급
  @GetMapping("/csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
    log.info("csrfToken 발급 = {}", csrfToken);

    return ResponseEntity.ok().build();
  }

  // 로그인
  @PostMapping(path = "/login")
  public ResponseEntity<UserDetails> login(
      @RequestBody @Valid DiscodeitUserDetails discodeitUserDetails) {
    log.info("로그인 요청: username={}", discodeitUserDetails.getUsername());
    UserDetails user = discodeitUserDetailsService.loadUserByUsername(
        discodeitUserDetails.getUsername());
    log.debug("로그인 응답: {}", user);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }
}
