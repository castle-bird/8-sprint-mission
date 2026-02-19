package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import com.sprint.mission.discodeit.exception.global.DiscodeitException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MessageExceptionHandler {

  // 못찾음 에러
  @ExceptionHandler(MessageNotFoundException.class)
  public ResponseEntity<ErrorResponse> messageNotFoundException(DiscodeitException e) {

    ErrorResponse errorResponse = ErrorResponse.from(e);

    return ResponseEntity
        .status(e.getStatus())
        .body(errorResponse);
  }

}
