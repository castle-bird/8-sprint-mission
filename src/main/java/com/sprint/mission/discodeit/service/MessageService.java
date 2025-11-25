package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // 등록
    UUID create(Message newUser);

    // 조회[단건]
    Message find(UUID id);

    // 조회[다건]
    List<Message> findMessages(List<UUID> ids);

    // 수정
    void update(Message updateMessage);

    // 삭제
    void delete(UUID id);
}
