package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;

public record UserStatusUpdateRequest(
    @NotNull
    @PastOrPresent // 미래의 시간 금지
    Instant newLastActiveAt
) {

}
