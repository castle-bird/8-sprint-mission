package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import com.sprint.mission.discodeit.exception.global.DiscodeitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

  // 못찾음 에러
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> userNotFoundException(DiscodeitException e) {

    ErrorResponse errorResponse = ErrorResponse.from(e);

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }

  // 중복에러
  @ExceptionHandler(UserDuplicateException.class)
  public ResponseEntity<ErrorResponse> userDuplicationException(DiscodeitException e) {

    ErrorResponse errorResponse = ErrorResponse.from(e);

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }
}
