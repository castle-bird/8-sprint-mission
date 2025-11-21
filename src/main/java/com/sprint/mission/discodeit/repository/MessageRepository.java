package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

    Message create(Message newMessage);

    Optional<Message> findById(UUID id);

    List<Message> findAll();

    Message modify(Message updatedMessage);

    void deleteById(UUID id);
}
