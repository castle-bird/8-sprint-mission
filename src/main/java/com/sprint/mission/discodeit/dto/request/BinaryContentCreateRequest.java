package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;

@Builder
public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

}
