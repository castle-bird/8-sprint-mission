package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 생성
    @Override
    public User create(User newUser) {

        Optional<User> findUser = userRepository.findById(newUser.getId());

        if (findUser.isPresent()) {
            return null;
        }

        return userRepository.create(newUser);
    }

    @Override
    public User findById(UUID id) {

        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> findAll() {
        List<User> allUsers = userRepository.findAll();

        if (allUsers.isEmpty()) {
            return null;
        }

        return allUsers.stream()
                .sorted(Comparator.comparing(User::getName))
                .toList();
    }

    @Override
    public User modify(UUID id, UserDTO updatedUser) {
        Optional<User> findUserOpt = userRepository.findById(id);

        if (findUserOpt.isEmpty()) {
            return null;
        }

        User findUser = findUserOpt.get();

        findUser.update(
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getPassword()
        );

        return userRepository.modify(findUser);
    }

    @Override
    public void deleteById(UUID id) {
        Optional<User> findUserOpt = userRepository.findById(id);

        if (findUserOpt.isPresent()) {
            userRepository.deleteById(id);
        }
    }
}
