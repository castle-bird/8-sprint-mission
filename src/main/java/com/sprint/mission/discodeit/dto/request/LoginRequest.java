package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;

@Builder
public record LoginRequest(
    String username,
    String password
) {

}
