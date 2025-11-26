package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDTO save(Message newMessage);

    MessageDTO findById(UUID id);

    List<MessageDTO> findAll();

    MessageDTO modify(Message updatedMessage);

    MessageDTO deleteById(UUID id);

    // DTO -> Entity 매핑
    default MessageDTO entityToDto(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getContent(),
                message.getUserId(),
                message.getChannelId()
        );
    }
}
