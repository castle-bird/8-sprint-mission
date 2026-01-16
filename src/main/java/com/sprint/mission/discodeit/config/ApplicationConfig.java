package com.sprint.mission.discodeit.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class ApplicationConfig {

  // Entity → DTO 변환시 매핑용
  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();

    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    return modelMapper;
  }
}
