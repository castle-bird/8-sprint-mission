package com.sprint.mission.discodeit.exception.global;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final Instant timestamp;
  private final String code;
  private final Map<String, Object> details;
  private final int status;

  // 주생성자:this() - 모든 필드 채움
  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode != null ? errorCode.getMessage() : "ErrorCode is NULL");

    if (errorCode == null) {
      throw new NullPointerException("ErrorCode는 필수입니다.");
    }

    this.timestamp = Instant.now();
    this.code = errorCode.name();
    this.details = details != null ? details : Map.of();
    this.status = errorCode.getStatus().value();
  }

  // 에러코드만 전달하는 생성자
  public DiscodeitException(ErrorCode errorCode) {
    this(errorCode, Map.of());
  }
}