package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository extends AbstractFileRepository<User> implements UserRepository {
    private static FileUserRepository instance;

    private FileUserRepository() {
        super(Path.of("data/users/"));
    }

    public static synchronized FileUserRepository getInstance() {
        if (instance == null) {
            instance = new FileUserRepository();
        }

        return instance;
    }

    @Override
    protected UUID getId(User result) {
        return result.getId();
    }

    // 생성
    @Override
    public User create(User newUser) {

        data.put(newUser.getId(), newUser);
        objectToFile(newUser);

        return newUser;
    }

    // 조회 [단건]
    @Override
    public Optional<User> findById(UUID id) {

        return Optional.ofNullable(data.get(id));
    }

    // 조회 [다건]
    @Override
    public List<User> findAll() {

        return new ArrayList<>(data.values());
    }

    // 수정
    @Override
    public User modify(User updatedUser) {

        data.put(updatedUser.getId(), updatedUser);
        objectToFile(updatedUser);

        return updatedUser;
    }

    @Override
    public void deleteById(UUID id) {

        data.remove(id);
        deleteFile(id);
    }
}
