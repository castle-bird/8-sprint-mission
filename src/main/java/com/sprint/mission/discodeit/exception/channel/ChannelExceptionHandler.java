package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import com.sprint.mission.discodeit.exception.global.DiscodeitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChannelExceptionHandler {

  // 못찾음 에러
  @ExceptionHandler(ChannelNotFoundException.class)
  public ResponseEntity<ErrorResponse> channelNotFoundException(DiscodeitException e) {

    ErrorResponse errorResponse = ErrorResponse.from(e);

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }

  // 비공개 채널 수정 불가 에러
  @ExceptionHandler(PrivateChannelModifyException.class)
  public ResponseEntity<ErrorResponse> privateChannelModifyException(DiscodeitException e) {

    ErrorResponse errorResponse = ErrorResponse.from(e);

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }
}
