package com.sprint.mission.discodeit.auth.config;

import com.sprint.mission.discodeit.auth.handler.CustomAccessDeniedHandler;
import com.sprint.mission.discodeit.auth.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.auth.handler.LoginSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      LoginSuccessHandler loginSuccessHandler, LoginFailureHandler loginFailureHandler,
      CustomAccessDeniedHandler customAccessDeniedHandler,
      DaoAuthenticationProvider authenticationProvider) throws Exception {
    return http.authorizeHttpRequests(
            auth -> auth.requestMatchers("/", "/index.html", "/favicon.ico", "/assets/**", "/error",
                    "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/auth/csrf-token").permitAll().anyRequest()
                .authenticated())
        // csrf
        .csrf(csrf -> csrf
            // csrf를 Cookie 방식으로 사용
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            // csrf 발급 및 검증처리
            .csrfTokenRequestHandler(spaCsrfTokenRequestHandler))
        // formLogin
        .formLogin(
            formLogin -> formLogin.loginPage("/index.html").loginProcessingUrl("/api/auth/login")
                .successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
                .permitAll())
        .exceptionHandling(ex -> ex
            // 인증되지 않은 사용자가 보호된 리소스에 접근했을 때 리다이렉트 대신 401 반환 (SPA를 위해)
            .authenticationEntryPoint((request, response, authException) -> {
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              response.setContentType("application/json");
              response.setCharacterEncoding("UTF-8");
              response.getWriter()
                  .write("{\"error\": \"UNAUTHORIZED\", \"message\": \"로그인이 필요합니다.\"}");
            })
            // 커스텀 핸들러(403 에러 핸들러)로 권한 없음 예외 처리
            .accessDeniedHandler(customAccessDeniedHandler))
        .authenticationProvider(authenticationProvider)
        .build();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {

    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
    authProvider.setPasswordEncoder(bCryptPasswordEncoder());

    return authProvider;
  }

  // 등록되는 Filter 목록 확인용
  @Bean
  public CommandLineRunner debugFilterChain(SecurityFilterChain securityFilterChain) {

    return args -> {
      int filterSize = securityFilterChain.getFilters().size();

      List<String> filterNames = IntStream.range(0, filterSize).mapToObj(
          idx -> String.format("\t[%s/%s] %s", idx + 1, filterSize,
              securityFilterChain.getFilters().get(idx).getClass())).toList();

      System.out.println("현재 적용된 필터 체인 목록:");
      filterNames.forEach(System.out::println);
    };
  }
}
