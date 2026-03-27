package com.sprint.mission.discodeit.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.auth.constants.AuthConstants;
import com.sprint.mission.discodeit.auth.dto.AuthErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

/**
 * 세션 만료 처리 전략
 * <p>
 * 동시 로그인 제한으로 인해 기존 세션이 만료되었을 때의 처리를 담당한다.
 * 통일된 JSON 형식의 응답을 반환하여 프론트엔드에서 적절히 처리할 수 있도록 한다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSessionExpiredStrategy implements SessionInformationExpiredStrategy {

  private final ObjectMapper objectMapper;

  @Override
  public void onExpiredSessionDetected(SessionInformationExpiredEvent event)
      throws IOException, ServletException {

    log.warn("{} 세션 만료 감지됨. 사용자: {}",
        AuthConstants.LOG_PREFIX_SESSION_EXPIRED, event.getSessionInformation().getPrincipal());

    HttpServletResponse response = event.getResponse();

    AuthErrorResponse errorResponse = new AuthErrorResponse(
        AuthConstants.ERROR_SESSION_EXPIRED,
        AuthConstants.MSG_SESSION_EXPIRED
    );

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
