package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // 등록
    Message insertMessage(Message newMessage);

    // 조회[특정 유저의 메세지]
    //List<Message> selectMessagesByUserId(UUID userId);

    // 조회[전체 메세지]
    List<Message> selectMessages();

    // 메세지 수정
    Message updateMessage(Message message);

    // 메세지 삭제
    Message deleteMessage(UUID id);
}
