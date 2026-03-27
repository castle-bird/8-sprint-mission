package com.sprint.mission.discodeit.auth.controller;

import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

  private final UserMapper userMapper;

  /**
   * 클라이언트에게 CSRF 토큰을 발급합니다.
   * 이 엔드포인트에 접근하는 것만으로도 Spring Security는 CsrfFilter를 통해
   * XSRF-TOKEN 쿠키를 응답에 포함시킵니다.
   */
  @GetMapping("/csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
    // CsrfToken을 파라미터로 받는 것만으로 토큰이 생성 및 응답 헤더에 추가됨
    log.info("CSRF 토큰 발급. 토큰 값: {}", csrfToken.getToken());
    return ResponseEntity.ok().build();
  }

  /**
   * 현재 인증된 사용자의 정보를 반환합니다.
   * @param userDetails Spring Security가 주입해주는 현재 사용자의 Principal 객체
   * @return 사용자 정보 DTO 또는 인증되지 않은 경우 401 응답
   */
  @GetMapping("/me")
  public ResponseEntity<UserDto> getUserDetails(
      @AuthenticationPrincipal DiscodeitUserDetails userDetails) {

    if (userDetails == null) {
      // @AuthenticationPrincipal이 null을 반환하는 경우는 일반적으로 인증되지 않았음을 의미
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    UserDto userDto = userMapper.toDto(userDetails.getUser());
    log.info("현재 로그인 사용자 정보 조회: {}", userDto.email());
    return ResponseEntity.ok(userDto);
  }
}
