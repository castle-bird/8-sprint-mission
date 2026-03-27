package com.sprint.mission.discodeit.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.function.Supplier;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {

  private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler(); // 토큰 그자체: SPA에서는 바로 전달
  private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler(); // 암호화: 일반 웹에선 암호화 우 전달

  // 최초 접속 시 CSRF 토큰 생성 및 쿠키 발급 강제
  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      Supplier<CsrfToken> csrfToken
  ) {
    this.xor.handle(request, response, csrfToken);

    csrfToken.get();
  }

  // HTTP 헤더 또는 파라미터에서 CSRF 토큰 값을 추출 및 검증
  // X-XSRF-TOKEN가 없으면 _csrf읽음
  @Override
  public String resolveCsrfTokenValue(
      HttpServletRequest request,
      CsrfToken csrfToken
  ) {
    String headerValue = request.getHeader(csrfToken.getHeaderName());

    return (StringUtils.hasText(headerValue) ? this.plain : this.xor)
        .resolveCsrfTokenValue(request, csrfToken);
  }
}
