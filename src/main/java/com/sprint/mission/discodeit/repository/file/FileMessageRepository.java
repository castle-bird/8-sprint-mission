package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileMessageRepository extends AbstractFileRepository<Message> implements MessageRepository {
    private static FileMessageRepository instance;

    private FileMessageRepository() {
        super(Path.of("data/messages/messages.ser"));
    }

    public static synchronized FileMessageRepository getInstance() {
        if (instance == null) {
            instance = new FileMessageRepository();
        }
        return instance;
    }

    @Override
    public Message create(Message newMessage) {
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
