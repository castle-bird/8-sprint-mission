package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // 생성
    @Override
    public Message create(Message newMessage) {

        Optional<Message> findMessage = messageRepository.findById(newMessage.getId());

        if (findMessage.isPresent()) {
            return null;
        }

        return messageRepository.create(newMessage);
    }

    // 조회 [단건]
    @Override
    public Message findById(UUID id) {

        return messageRepository.findById(id).orElse(null);
    }

    // 조회 [다건]
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

    // 수정
    @Override
    public Message modify(UUID id, MessageDTO updatedMessage) {
        Optional<Message> findMessageOpt = messageRepository.findById(id);

        if (findMessageOpt.isEmpty()) {
            return null;
        }

        Message findMessage = findMessageOpt.get();

        findMessage.update(
                updatedMessage.getContent()
        );

        return messageRepository.modify(findMessage);
    }

    // 삭제
    @Override
    public void deleteById(UUID id) {
        Optional<Message> findMessage = messageRepository.findById(id);

        if (findMessage.isPresent()) {
            messageRepository.deleteById(id);
        }
    }
}
