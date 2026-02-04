package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.exception.global.DiscodeitException;
import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
    Instant timestamp,
    String code,
    String message,
    Map<String, Object> details,
    String exceptionType,
    int status
) {

  // 에러만 던져주면 알아서 만들기
  // e.g. ErrorResponse er = ErrorResponse.from(e)
  public static ErrorResponse from(DiscodeitException e) {
    return new ErrorResponse(
        e.getTimestamp(),
        e.getCode(),
        e.getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        e.getStatus()
    );
  }
}