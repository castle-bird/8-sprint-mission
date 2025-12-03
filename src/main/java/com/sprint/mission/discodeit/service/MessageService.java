package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(Message newMessage);

    Message findById(UUID id);

    List<Message> findAll();

    Message modify(Message updatedMessage);

    Message deleteById(UUID id);
}
