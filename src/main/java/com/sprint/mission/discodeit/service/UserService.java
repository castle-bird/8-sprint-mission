package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDTO save(User newUser);

    UserDTO findById(UUID id);

    List<UserDTO> findAll();

    UserDTO modify(User updatedUser);

    UserDTO deleteById(UUID id);

    // DTO -> Entity 매핑
    default UserDTO entityToDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
