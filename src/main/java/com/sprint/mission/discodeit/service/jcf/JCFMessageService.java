package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    // 메세지 목록 저장을 위한 List
    private final List<Message> messages;

    public JCFMessageService() {
        messages = new ArrayList<>();
    }

    @Override
    public UUID create(Message newUser) {
        messages.add(newUser);

        return newUser.getId();
    }

    @Override
    public Message find(UUID id) {
        return messages.stream().filter(message -> message.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Message> findMessages(List<UUID> ids) {
        return messages.stream().filter(message -> ids.contains(message.getId())).toList();
    }

    @Override
    public void update(Message updateMessage) {
        Message findMessage = messages.stream()
                .filter(message -> message.getId().equals(updateMessage.getId()))
                .findFirst()
                .orElse(null);

        if (findMessage != null) {
            findMessage.setUpdatedAt(System.currentTimeMillis());
            findMessage.setContent(updateMessage.getContent());
        } else {
            System.out.println("입력하신 메세지 정보가 존재하지 않습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        Message findMessage = messages.stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (findMessage != null) {
            messages.remove(findMessage);
        } else {
            System.out.println("입력하신 메세지 정보가 존재하지 않습니다.");
        }
    }
}
