package com.sprint.mission.discodeit.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.mapper.UserMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final UserMapper userMapper;
  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    Object principal = authentication.getPrincipal();
    if (principal instanceof DiscodeitUserDetails userDetails) {
      UserDto userDto = userMapper.toDto(userDetails.getUser());

      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");

      response.getWriter().write(objectMapper.writeValueAsString(userDto));

      log.info("[LoginSuccessHandler] 로그인 성공. 사용자: {}", userDetails.getUsername());
    } else {
      log.error("[LoginSuccessHandler] 예상치 못한 Principal 타입: {}", principal.getClass().getName());

      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write("{\"error\": \"인증 정보를 처리하는 중 오류가 발생했습니다.\"}");
    }
  }
}
