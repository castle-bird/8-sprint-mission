package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MDCLoggingInterceptor implements HandlerInterceptor {

  /**
   * 컨트롤러가 실행되기 전(Pre-handle) 호출됨.
   * 각 요청에 대해 고유 ID를 생성하고 MDC 문맥에 저장함.
   */
  @Override
  public boolean preHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler
  ) {
    String requestId = UUID.randomUUID().toString();

    // MDC에 요청 정보 저장 (Logback/Log4j2 등의 설정에서 %X{request_id} 등으로 꺼내 쓸 수 있음)
    MDC.put("request_id", requestId);
    MDC.put("request_method", request.getMethod());
    MDC.put("request_url", request.getRequestURI());

    response.setHeader("Discodeit-Request-ID", requestId);
    return true; // true를 반환해야 다음 인터셉터나 컨트롤러로 진행됨
  }

  /**
   * 뷰 렌더링까지 모두 끝난 후(After Completion) 호출됨.
   * 요청 처리가 완료되었으므로 MDC에 저장된 정보를 반드시 제거해야 함.
   */
  @Override
  public void afterCompletion(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      Exception ex
  ) {
    MDC.clear();
  }
}
