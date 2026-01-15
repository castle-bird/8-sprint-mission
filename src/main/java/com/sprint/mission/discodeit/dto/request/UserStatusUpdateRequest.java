package com.sprint.mission.discodeit.dto.request;

import java.time.Instant;
import lombok.Builder;

@Builder
public record UserStatusUpdateRequest(
    Instant newLastActiveAt
) {

}
