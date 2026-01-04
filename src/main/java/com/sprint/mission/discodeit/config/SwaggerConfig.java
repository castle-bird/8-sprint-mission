package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Channel;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {

    return new OpenAPI()
        .components(new Components())
        .info(
            new Info()
                .title("Discodeit(디스코드잇) API")
                .description("Discodeit API 입니다.")
                .version("1.0.0")
        );
  }
}
