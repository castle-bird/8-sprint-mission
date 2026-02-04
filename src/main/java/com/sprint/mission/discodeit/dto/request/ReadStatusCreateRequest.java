package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotNull
    UUID userId,

    @NotNull
    UUID channelId,

    @NotNull
    @PastOrPresent // 미래의 시간 금지
    Instant lastReadAt
) {

}
