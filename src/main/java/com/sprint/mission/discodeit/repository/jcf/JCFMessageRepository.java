package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    // 싱글턴 패턴으로 데이터 공유
    // Repository에선 CRUD작업에 관한 작업 중, 데이터 유/무 체크과정 등등을 하지 않는다.
    // Service단에서 데이터 유/무 체크 관련한 작업을 한다.

    private static JCFMessageRepository instance;
    private final Map<UUID, Message> data = new HashMap<>();

    private JCFMessageRepository() {
        // 기본생성자 private로 객체 내부에서만 사용가능하게
    }

    public static synchronized JCFMessageRepository getInstance() {
        if (instance == null) {
            instance = new JCFMessageRepository();
        }
        return instance;
    }

    @Override
    public Message create(Message newMessage) {
        data.put(newMessage.getId(), newMessage);
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
        return updatedMessage;
    }

    @Override
    public Message deleteById(UUID id) {
        Message findMessage = data.get(id);

        data.remove(id);

        return findMessage;
    }
}
