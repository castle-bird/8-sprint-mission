package com.sprint.mission.discodeit.auth.dto;

/**
 * CSRF 토큰 응답 DTO
 * <p>
 * 클라이언트에게 제공하는 CSRF 토큰 정보를 표준화된 형식으로 제공한다.
 */
public record CsrfTokenResponse(
    String token,
    String headerName,
    String parameterName
) {

}

