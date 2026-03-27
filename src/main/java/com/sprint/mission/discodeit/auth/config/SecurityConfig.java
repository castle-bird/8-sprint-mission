package com.sprint.mission.discodeit.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.auth.constants.AuthConstants;
import com.sprint.mission.discodeit.auth.dto.AuthErrorResponse;
import com.sprint.mission.discodeit.auth.handler.CustomAccessDeniedHandler;
import com.sprint.mission.discodeit.auth.handler.CustomSessionExpiredStrategy;
import com.sprint.mission.discodeit.auth.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.auth.handler.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final SpaCsrfTokenRequestHandler spaCsrfTokenRequestHandler;
  private final CustomSessionExpiredStrategy customSessionExpiredStrategy;
  private final ObjectMapper objectMapper;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      LoginSuccessHandler loginSuccessHandler,
      LoginFailureHandler loginFailureHandler,
      CustomAccessDeniedHandler customAccessDeniedHandler,
      DaoAuthenticationProvider authenticationProvider
  ) throws Exception {
    http
        // 접근 권한 설정
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/",
                "/index.html",
                "/favicon.ico",
                "/assets/**",
                "/error",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api/auth/login",
                "/api/auth/logout"
            ).permitAll()
            .requestMatchers(HttpMethod.GET, "/api/auth/csrf-token").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // 회원가입
            .anyRequest().authenticated()
        )
        // CSRF 설정
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(spaCsrfTokenRequestHandler)
            // 로그인, 로그아웃, 회원가입은 CSRF 검증에서 제외
            .ignoringRequestMatchers(
                "/api/auth/login",
                "/api/auth/logout",
                "/api/users"
            )
        )
        // 폼 로그인 설정
        .formLogin(formLogin -> formLogin
            .loginPage("/index.html")
            .loginProcessingUrl("/api/auth/login")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
            .permitAll()
        )
        // 로그아웃 설정
        .logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .deleteCookies(AuthConstants.COOKIE_SESSION_ID, AuthConstants.COOKIE_XSRF_TOKEN)
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutSuccessHandler((request, response, authentication) ->
                response.setStatus(200)
            )
        )
        // 예외 처리 설정
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, authException) -> {
              AuthErrorResponse errorResponse = new AuthErrorResponse(
                  AuthConstants.ERROR_UNAUTHORIZED,
                  AuthConstants.MSG_LOGIN_REQUIRED
              );

              response.setStatus(401);
              response.setContentType(MediaType.APPLICATION_JSON_VALUE);
              response.setCharacterEncoding("UTF-8");
              response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            })
            .accessDeniedHandler(customAccessDeniedHandler)
        )
        // 동시 로그인 제한 및 세션 설정
        .sessionManagement(session -> session
            .maximumSessions(1)
            .expiredSessionStrategy(customSessionExpiredStrategy)
        )
        // 인증 제공자
        .authenticationProvider(authenticationProvider);

    return http.build();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
    authProvider.setPasswordEncoder(bCryptPasswordEncoder());
    return authProvider;
  }
}
