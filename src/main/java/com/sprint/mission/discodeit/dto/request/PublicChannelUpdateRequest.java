package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;

@Builder
public record PublicChannelUpdateRequest(
    String newName,
    String newDescription
) {

}
