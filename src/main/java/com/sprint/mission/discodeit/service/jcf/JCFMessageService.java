package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messagesData;

    public JCFMessageService() {
        messagesData = new HashMap<>();
    }

    @Override
    public MessageDTO save(Message newMessage) {
        messagesData.put(newMessage.getId(), newMessage);

        return entityToDto(
                messagesData.get(newMessage.getId())
        );
    }

    @Override
    public MessageDTO findById(UUID id) {
        Message findMessage = messagesData.get(id);

        if (findMessage == null) {
            System.out.println("[JCFMessageService] findById(): 존재하지 않는 메세지입니다.");

            return null;
        }

        return entityToDto(findMessage);
    }

    @Override
    public List<MessageDTO> findAll() {
        List<Message> allMessages = messagesData.values().stream().toList();

        if (allMessages.isEmpty()) {
            System.out.println("[JCFMessageService] findAll(): 메세지가 존재하지 않습니다.");

            return null;
        }

        return allMessages.stream().map(this::entityToDto).toList();
    }

    @Override
    public MessageDTO modify(Message updatedMessage) {
        Message findMessage = messagesData.get(updatedMessage.getId());

        if (findMessage == null) {
            System.out.println("[JCFMessageService] modify(): 메세지가 존재하지 않습니다.");

            return null;
        }

        findMessage.update(updatedMessage.getContent());

        return entityToDto(findMessage);
    }

    @Override
    public MessageDTO deleteById(UUID id) {
        Message findMessage = messagesData.get(id);

        if (findMessage == null) {
            System.out.println("[JCFMessageService] deleteById(): 메세지가 존재하지 않습니다.");

            return null;
        }

        messagesData.remove(id);

        return entityToDto(findMessage);
    }
}
