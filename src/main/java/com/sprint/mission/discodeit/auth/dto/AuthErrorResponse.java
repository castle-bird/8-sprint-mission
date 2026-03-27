package com.sprint.mission.discodeit.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 인증 에러 응답 DTO
 * <p>
 * 로그인 실패, 접근 거부 등 인증 관련 에러 응답을 통일된 형식으로 제공한다.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthErrorResponse(
    boolean success,
    String error,
    String message
) {

  public AuthErrorResponse(String error, String message) {
    this(false, error, message);
  }
}

