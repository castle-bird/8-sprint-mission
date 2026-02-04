package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    @NotEmpty(message = "참여자는 최소 2명 이상이어야 합니다.")
    @Size(min = 2)
    List<UUID> participantIds
) {

}