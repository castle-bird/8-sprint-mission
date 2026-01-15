package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userID,
    UUID channelId,
    Instant lastReadAt
) {

}
