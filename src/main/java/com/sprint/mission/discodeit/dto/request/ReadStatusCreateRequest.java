package com.sprint.mission.discodeit.dto.request;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ReadStatusCreateRequest(
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
