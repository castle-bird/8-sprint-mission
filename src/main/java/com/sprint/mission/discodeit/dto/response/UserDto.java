package com.sprint.mission.discodeit.dto.response;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online
) {

}
