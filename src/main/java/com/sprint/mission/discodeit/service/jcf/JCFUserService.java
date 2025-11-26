package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> usersData;

    public JCFUserService() {
        usersData = new HashMap<>();
    }

    @Override
    public UserDTO save(User newUser) {
        usersData.put(newUser.getId(), newUser);

        return entityToDto(
                usersData.get(newUser.getId())
        );
    }

    @Override
    public UserDTO findById(UUID id) {
        User findUser = usersData.get(id);

        if (findUser == null) {
            System.out.println("[JCFUserService] findById(): 존재하지 않는 사용자입니다.");
            // throw new RuntimeException("존재하지 않는 사용자입니다.");

            return null;
        }

        return entityToDto(findUser);
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> allUsers = new ArrayList<>(usersData.values());

        if (allUsers.isEmpty()) {
            System.out.println("[JCFUserService] findAll(): 사용자가 0명 입니다.");
        }

        return allUsers.stream().map(this::entityToDto).toList();
    }

    @Override
    public UserDTO modify(User updatedUser) {
        User findUser = usersData.get(updatedUser.getId());

        if (findUser == null) {
            System.out.println("[JCFUserService] modify(): 존재하지 않는 사용자입니다.");
            // throw new RuntimeException("존재하지 않는 사용자입니다.");
            return null;
        }

        findUser.update(
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getPassword()
        );

        return entityToDto(findUser);
    }

    @Override
    public UserDTO deleteById(UUID id) {
        User findUser = usersData.get(id);

        if (findUser == null) {
            System.out.println("[JCFUserService] deleteById(): 존재하지 않는 사용자입니다.");
            // throw new RuntimeException("존재하지 않는 사용자입니다.");

            return null;
        }

        usersData.remove(id);

        return entityToDto(findUser);
    }
}
