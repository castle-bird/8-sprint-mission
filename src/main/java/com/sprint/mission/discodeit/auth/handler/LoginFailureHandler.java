package com.sprint.mission.discodeit.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception
  ) throws IOException, ServletException {
    String errorMessage = determineErrorMessage(exception);

    // JSON 에러 응답 생성
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("success", false);
    errorResponse.put("error", "AUTHENTICATION_FAILED");
    errorResponse.put("message", errorMessage);

    // 응답 헤더 설정
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

    // JSON 응답 전송
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

    log.info("[LoginSuccessHandler] 로그인 실패");
  }


  /**
   * 예외 타입에 따른 에러 메시지 결정
   */
  private String determineErrorMessage(AuthenticationException exception) {
    if (exception instanceof BadCredentialsException) {
      return "아이디 또는 비밀번호가 올바르지 않습니다.";
    } else if (exception instanceof DisabledException) {
      return "비활성화된 계정입니다.";
    } else {
      return "로그인에 실패했습니다.";
    }
  }
}
