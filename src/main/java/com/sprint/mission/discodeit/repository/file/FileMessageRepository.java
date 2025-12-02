package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private static FileMessageRepository instance;
    private final Path path = Path.of("data/message.ser");
    private Map<UUID, Message> data;

    private FileMessageRepository() {
        loadFile();
    }

    public static synchronized FileMessageRepository getInstance() {
        if (instance == null) {
            instance = new FileMessageRepository();
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
            data = (Map<UUID, Message>) obj;

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
    public Message save(Message newMessage) {
        data.put(newMessage.getId(), newMessage);

        saveFile();
        return newMessage;
    }

    @Override
    public Message findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message modify(Message updatedMessage) {
        data.put(updatedMessage.getId(), updatedMessage);

        saveFile();
        return updatedMessage;
    }

    @Override
    public Message deleteById(UUID id) {
        Message findMessage = data.get(id);

        data.remove(id);
        saveFile();
        return findMessage;
    }
}
