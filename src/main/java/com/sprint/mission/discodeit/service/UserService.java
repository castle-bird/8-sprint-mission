package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    // 등록
    UUID create(User newUser);

    // 조회[단건]
    User find(UUID id);

    // 조회[다건]
    List<User> findUsers(List<UUID> ids);

    // 수정
    void update(User updateUser);

    // 삭제
    void delete(UUID id);
}
