package com.sprint.mission.discodeit.auth.controller;

import com.sprint.mission.discodeit.auth.config.SpaCsrfTokenRequestHandler;
import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetailsService;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
  private final UserMapper userMapper;

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

  @GetMapping("/me")
  public ResponseEntity<UserDto> getUserDetails(
      @AuthenticationPrincipal DiscodeitUserDetails userDetails) {

    // 1. 인증 정보가 없는 경우 (Security에서 걸러지지만 방어 코드 작성)
    if (userDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 2. UserDetails 내부의 User 엔티티를 UserDto로 변환하여 반환
    UserDto userDto = userMapper.toDto(userDetails.getUser());

    log.info("현재 로그인 사용자 조회: {}", userDto.email());
    return ResponseEntity.ok(userDto);
  }
}
