package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    // 유저 목록 저장을 위한 List
    private final List<User> users;

    public JCFUserService() {
        users = new ArrayList<>();
    }

    @Override
    public UUID create(User newUser) {
        users.add(newUser);

        return newUser.getId();
    }

    @Override
    public User find(UUID id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<User> findUsers(List<UUID> ids) {
        return users.stream().filter(user -> ids.contains(user.getId())).toList();
    }

    @Override
    public void update(User updateUser) {
        User findUser = users.stream()
                .filter(user -> user.getId().equals(updateUser.getId()))
                .findFirst()
                .orElse(null);

        if (findUser != null) {
            findUser.setName(updateUser.getName());
            findUser.setPassword(updateUser.getPassword());
            findUser.setUpdatedAt(System.currentTimeMillis());
        } else {
            System.out.println("입력하신 사용자가 존재하지 않습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        User findUser = users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (findUser != null) {
            users.remove(findUser);
        } else {
            System.out.println("입력하신 사용자가 존재하지 않습니다.");
        }
    }
}
