package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User newUser) {
        // 이미 있는 유저인지 체크
        User findUser = userRepository.findById(newUser.getId());

        if (findUser != null) {
            return null;
        }

        return userRepository.create(newUser);
    }

    @Override
    public User findById(UUID id) {
        User findUser = userRepository.findById(id);

        // 유저가 있는지
        if (findUser == null) {
            return null;
        }

        return findUser;
    }

    @Override
    public List<User> findAll() {
        List<User> allUsers = userRepository.findAll();

        if(allUsers.isEmpty()) {
            return null;
        }

        return allUsers.stream()
                .sorted(Comparator.comparing(User::getName))
                .toList();
    }

    @Override
    public User modify(User updatedUser) {
        User findUser = userRepository.findById(updatedUser.getId());

        if (findUser == null) {
            return null;
        }

        findUser.update(
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getPassword()
        );

        return userRepository.modify(findUser);
    }

    @Override
    public User deleteById(UUID id) {
        User findUser = userRepository.deleteById(id);

        if (findUser == null) {
            return null;
        }

        userRepository.deleteById(id);

        return findUser;
    }
}
