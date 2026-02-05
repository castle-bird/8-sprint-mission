package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    
    @Size(min = 2, max = 50)
    String newUsername,

    @Email
    @Size(max = 100)
    String newEmail,

    @Size(min = 8, max = 20)
    String newPassword
) {

}
