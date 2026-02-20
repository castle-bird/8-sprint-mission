package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record BinaryContentCreateRequest(

    @NotBlank
    @Size(min = 2, max = 255)
    String fileName,

    @NotBlank
    @Size(min = 2, max = 100)
    String contentType,

    @NotEmpty
    @Size(min = 1)
    byte[] bytes
) {

}
