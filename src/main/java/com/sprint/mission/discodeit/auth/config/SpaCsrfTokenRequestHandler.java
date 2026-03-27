package com.sprint.mission.discodeit.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.function.Supplier;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.stereotype.Component;

/**
 * A custom CsrfTokenRequestHandler for Single Page Applications (SPAs).
 * <p>
 * This implementation delegates all operations to the default {@link XorCsrfTokenRequestAttributeHandler}.
 * It exists to be explicitly wired into the security configuration, making the CSRF handling strategy clear.
 * For SPAs, the handler resolves the token from the {@code X-XSRF-TOKEN} header or the {@code _csrf} parameter.
 */
@Component
public final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {

    private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        this.delegate.handle(request, response, csrfToken);
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        return this.delegate.resolveCsrfTokenValue(request, csrfToken);
    }
}
