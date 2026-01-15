package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageCreateRequest(
    String content,
    UUID channelId,
    UUID authorId
) {

}
