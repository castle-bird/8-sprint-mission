package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // 등록
    User insertUser(User newUser);

    // 조회[단건]
    User selectUser(UUID id);

    // 조회[다건]
    List<User> selectUsers(List<UUID> ids);

    // 수정 - return 값으로 수정된 데이터 넘기기
    User updateUser(User user);

    // 삭제 - return 값으로 삭제한 데이터 넘기기
    User deleteUser(UUID id);
}
