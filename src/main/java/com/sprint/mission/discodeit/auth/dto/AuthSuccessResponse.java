package com.sprint.mission.discodeit.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 인증 성공 응답 DTO
 * <p>
 * 로그인 성공 등 인증 관련 성공 응답을 통일된 형식으로 제공한다.
 * 제네릭을 사용하여 다양한 응답 데이터 타입을 지원한다.
 *
 * @param <T> 응답 데이터 타입
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthSuccessResponse<T>(
    boolean success,
    T data,
    String message
) {

  public AuthSuccessResponse(T data) {
    this(true, data, null);
  }

  public AuthSuccessResponse(T data, String message) {
    this(true, data, message);
  }
}

