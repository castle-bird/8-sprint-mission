package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {

    private  final List<User> users;

    public JCFUserService() {
        this.users = new ArrayList<>();
    }

    @Override
    public User insertUser(User newUser) {
        users.add(newUser);
        return newUser;
    }

    @Override
    public User selectUser(UUID id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<User> selectUsers(List<UUID> ids) {
        return users.stream().filter(user -> ids.contains(user.getId())).collect(Collectors.toList());
    }

    @Override
    public User updateUser(User updatedUser) {
        User findUser = users.stream().filter(user -> user.getId().equals(updatedUser.getId())).findFirst().orElse(null);
        // Optional: null에러 안나게 null이 나올 수 있는 값을 감싸주는 Wrapper클래스이다.
        // findFirst():  filter 조건에 일치하는 element 1개를 Optional로 리턴
        // .orElse(null): Optional이 비어 있으면 null 반환, 값이 있으면 해당 값 반환

        // 위에서 만약 null이 나오면 set을 할 수 없기에 assert로 null 체크하고 넘어가야한다.
        assert findUser != null;

        findUser.setName(updatedUser.getName());
        findUser.setNickname(updatedUser.getName());

        return findUser;
    }

    @Override
    public User deleteUser(UUID id) {
        User findUser = users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);

        // 위에서 만약 null이 나오면 set을 할 수 없기에 assert로 null 체크하고 넘어가야한다.
        assert findUser != null;

        users.remove(findUser);

        return findUser;
    }
}
