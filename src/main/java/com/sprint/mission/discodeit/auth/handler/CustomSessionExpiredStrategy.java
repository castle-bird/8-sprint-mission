package com.sprint.mission.discodeit.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
 * JSON 기반 응답을 반환하여 프론트엔드에서 적절히 처리할 수 있도록 한다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSessionExpiredStrategy implements SessionInformationExpiredStrategy {

  private final ObjectMapper objectMapper;

  @Override
  public void onExpiredSessionDetected(SessionInformationExpiredEvent event)
      throws IOException, ServletException {

    log.warn("[CustomSessionExpiredStrategy] 세션 만료 감지됨. 사용자: {}", event.getSessionInformation().getPrincipal());

    HttpServletResponse response = event.getResponse();

    // 세션 만료 안내 메시지 작성
    Map<String, Object> result = new HashMap<>();
    result.put("success", false);
    result.put("error", "SESSION_EXPIRED");
    result.put("message", "다른 기기에서의 로그인으로 인해 현재 세션이 만료되었습니다. 다시 로그인해주세요.");

    // JSON 응답 설정
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    response.getWriter().write(objectMapper.writeValueAsString(result));
  }
}
