package com.sprint.mission.discodeit.exception.global;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final Instant timestamp;
  private final ErrorCode errorCode;
  private final Map<String, Object> details;

  // 주생성자:this() - 모든 필드 채움
  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getMessage());
    this.timestamp = Instant.now();
    this.errorCode = Objects.requireNonNull(errorCode);
    this.details = details != null ? details : Map.of();
  }

  // 에러코드만 전달하는 생성자
  public DiscodeitException(ErrorCode errorCode) {
    this(errorCode, Map.of());
  }

}