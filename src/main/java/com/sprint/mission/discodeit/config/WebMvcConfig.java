package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final MDCLoggingInterceptor mdcLoggingInterceptor;

  public WebMvcConfig(MDCLoggingInterceptor mdcLoggingInterceptor) {
    this.mdcLoggingInterceptor = mdcLoggingInterceptor;
  }

  /**
   * 애플리케이션에 인터셉터를 등록하는 메서드.
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(mdcLoggingInterceptor)
        .addPathPatterns("/**"); // 모든 경로에 대해 인터셉터 적용
    //.excludePathPatterns("/css/**", "/js/**", "/images/**"); 추후 정적 리소스에 대해서 제거 가능
  }
}
