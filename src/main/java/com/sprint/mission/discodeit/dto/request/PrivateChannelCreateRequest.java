package com.sprint.mission.discodeit.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record PrivateChannelCreateRequest(
    List<UUID> participantIds
) {

}
