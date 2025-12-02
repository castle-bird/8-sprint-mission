package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private static FileUserRepository instance;
    private final Path path = Path.of("data/users.ser");
    private Map<UUID, User> data;

    private FileUserRepository() {
        loadFile();
    }

    public static synchronized FileUserRepository getInstance() {
        if (instance == null) {
            instance = new FileUserRepository();
        }

        return instance;
    }

    // File > Object
    private void loadFile() {
        if (!Files.exists(path)) {
            data = new HashMap<>();

            return;
        }

        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            Object obj = ois.readObject();
            data = (Map<UUID, User>) obj;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Object > File
    private void saveFile() {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User save(User newUser) {
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
