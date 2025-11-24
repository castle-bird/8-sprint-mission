package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class JCFUserService implements UserService {
    // 유저 목록 저장을 위한 List
    private final List<User> users;

    public JCFUserService() {
        users = new ArrayList<>();
    }


}
