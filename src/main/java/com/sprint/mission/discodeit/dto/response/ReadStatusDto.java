package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ReadStatusDto(
    UUID id,
    UUID userID,
    UUID channelId,
    Instant lastReadAt
) {

}
