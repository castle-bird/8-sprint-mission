package com.sprint.mission.discodeit.dto.response;

import java.util.UUID;

public record MessageAttachmentsDto(
    UUID messageId,
    UUID attachmentId
) {

}
