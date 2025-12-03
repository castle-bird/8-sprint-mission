package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User create(User newUser);

    User findById(UUID id);

    List<User> findAll();

    User modify(User updatedUser);

    User deleteById(UUID id);
}
