package com.sprint.mission.discodeit.exception.global;

public enum ErrorCode {
  // 사용자 에러
  USER_ERROR("사용자 에러"),
  USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
  USER_DUPLICATE("이미 존재하는 사용자 이름입니다."),

  // 체널 에러
  CHANNEL_ERROR("채널을 에러"),
  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정 불가능합니다."),

  // 메세지 에러
  MESSAGE_ERROR("메세지 에러"),
  MESSAGE_NOT_FOUND("메세지를 찾을 수 없습니다.");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
