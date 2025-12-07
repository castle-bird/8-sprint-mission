package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository extends AbstractFileRepository<Message> implements MessageRepository {
    private static FileMessageRepository instance;

    private FileMessageRepository() {
        super(Path.of("data/messages/"));
    }

    public static synchronized FileMessageRepository getInstance() {
        if (instance == null) {
            instance = new FileMessageRepository();
        }
        return instance;
    }

    @Override
    protected UUID getId(Message result) {
        return result.getId();
    }

    // 생성
    @Override
    public Message create(Message newMessage) {

        data.put(newMessage.getId(), newMessage);
        objectToFile(newMessage);

        return newMessage;
    }

    // 조회 [단건]
    @Override
    public Optional<Message> findById(UUID id) {

        return Optional.ofNullable(data.get(id));
    }

    // 조회 [다건]
    @Override
    public List<Message> findAll() {

        return new ArrayList<>(data.values());
    }

    // 수정
    @Override
    public Message modify(Message updatedMessage) {

        data.put(updatedMessage.getId(), updatedMessage);
        objectToFile(updatedMessage);

        return updatedMessage;
    }

    // 삭제
    @Override
    public void deleteById(UUID id) {

        data.remove(id);
        deleteFile(id);
    }
}
