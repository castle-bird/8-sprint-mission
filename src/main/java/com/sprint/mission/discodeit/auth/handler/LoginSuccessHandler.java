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

    if (authentication.getPrincipal() instanceof DiscodeitUserDetails that) {
      UserDto userDto = userMapper.toDto(that.getUser());

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpServletResponse.SC_OK);

      response.getWriter().write(objectMapper.writeValueAsString(userDto));

      log.info("[LoginSuccessHandler] 로그인 성공");
    } else {
      // 예상치 못한 Principal 타입일 경우
      log.error("[LoginSuccessHandler] 예상치 못한 Principal 타입");

      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 에러
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write("{\"error\": \"인증 정보를 처리하는 중 오류가 발생했습니다.\"}");
    }

  }
}
