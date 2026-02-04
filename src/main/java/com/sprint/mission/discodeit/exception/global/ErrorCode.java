package com.sprint.mission.discodeit.exception.global;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  // 사용자 에러
  USER_ERROR("사용자 에러", HttpStatus.BAD_REQUEST), // 400
  USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND), // 404
  USER_DUPLICATE("이미 존재하는 사용자 정보입니다.", HttpStatus.CONFLICT), // 409

  // 채널 에러
  CHANNEL_ERROR("채널 에러", HttpStatus.BAD_REQUEST), // 400
  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다.", HttpStatus.NOT_FOUND), // 404
  PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정 불가능합니다.", HttpStatus.BAD_REQUEST), // 400

  // 메세지 에러
  MESSAGE_ERROR("메세지 에러", HttpStatus.BAD_REQUEST), // 400
  MESSAGE_NOT_FOUND("메세지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND); // 404

  private final String message;
  private final HttpStatus status;

  ErrorCode(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
  }
}