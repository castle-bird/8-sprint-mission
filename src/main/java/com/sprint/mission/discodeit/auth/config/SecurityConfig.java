package com.sprint.mission.discodeit.auth.config;

import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final SpaCsrfTokenRequestHandler spaCsrfTokenRequestHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(
        csrf -> csrf
            .csrfTokenRepository(
                CookieCsrfTokenRepository.withHttpOnlyFalse()) // csrf를 Cookie 방식으로 사용
            .csrfTokenRequestHandler(spaCsrfTokenRequestHandler)
    ).build();
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
