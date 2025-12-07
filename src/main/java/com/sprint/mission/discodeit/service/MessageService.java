package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(Message newMessage);

    Message findById(UUID id);

    List<Message> findAll();

    Message modify(UUID id, MessageDTO updatedMessage);

    void deleteById(UUID id);
}
