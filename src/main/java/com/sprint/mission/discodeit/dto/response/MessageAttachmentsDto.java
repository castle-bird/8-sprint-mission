package com.sprint.mission.discodeit.dto.response;

import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageAttachmentsDto(
    UUID messageId,
    UUID attachmentId
) {

}
