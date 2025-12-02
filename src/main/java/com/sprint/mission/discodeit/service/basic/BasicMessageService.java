package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message save(Message newMessage) {
        Message findMessage = messageRepository.findById(newMessage.getId());

        if (findMessage != null) {
            return null;
        }

        return messageRepository.save(newMessage);
    }

    @Override
    public Message findById(UUID id) {
        Message findMessage = messageRepository.findById(id);

        if (findMessage == null) {
            return null;
        }

        return findMessage;
    }

    @Override
    public List<Message> findAll() {
        List<Message> allMessage = messageRepository.findAll();

        if (allMessage.isEmpty()) {
            return null;
        }

        return allMessage.stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .toList();
    }

    @Override
    public Message modify(Message updatedMessage) {
        Message findMessage = messageRepository.findById(updatedMessage.getId());

        if (findMessage == null) {
            return null;
        }

        findMessage.update(
                updatedMessage.getContent()
        );

        return messageRepository.modify(findMessage);
    }

    @Override
    public Message deleteById(UUID id) {
        Message findMessage = messageRepository.findById(id);

        if (findMessage == null) {
            return null;
        }

        return messageRepository.deleteById(id);
    }
}
