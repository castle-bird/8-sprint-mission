package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserRepository extends AbstractFileRepository<User> implements UserRepository {
    private static FileUserRepository instance;

    private FileUserRepository() {
        super(Path.of("data/users/users.ser"));
    }

    public static synchronized FileUserRepository getInstance() {
        if (instance == null) {
            instance = new FileUserRepository();
        }

        return instance;
    }

    @Override
    public User create(User newUser) {
        data.put(newUser.getId(), newUser);

        saveFile();
        return newUser;
    }

    @Override
    public User findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User modify(User updatedUser) {
        data.put(updatedUser.getId(), updatedUser);

        saveFile();
        return updatedUser;
    }

    @Override
    public User deleteById(UUID id) {
        User findUser = data.get(id);

        data.remove(id);

        saveFile();
        return findUser;
    }
}
