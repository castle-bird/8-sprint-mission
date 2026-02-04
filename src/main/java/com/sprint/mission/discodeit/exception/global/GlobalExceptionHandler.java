package com.sprint.mission.discodeit.exception.global;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * DTO의 @Valid 검증 실패 시 발생하는 예외 처리
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e
  ) {

    Map<String, Object> details = new HashMap<>();

    // e.getBindingResult(): 발생한 검증 오류 정보
    // getFieldErrors(): 그중 필드 단위의 에러 리스트를 추출
    e.getBindingResult().getFieldErrors().forEach(error ->
        details.put(error.getField(), error.getDefaultMessage())
    );

    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        "INVALID_INPUT_VALUE",
        "입력값이 유효하지 않습니다.",
        details,
        e.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }
}
