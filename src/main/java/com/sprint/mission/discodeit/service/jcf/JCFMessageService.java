package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final UserService userService; // 유저별 메세지 검색을 위함
    private final List<Message> messages;

    public JCFMessageService() {
        this.messages = new ArrayList<>();
        this.userService = new JCFUserService();
    }

    @Override
    public Message insertMessage(Message newMessage) {
        messages.add(newMessage);
        return newMessage;
    }


    @Override
    public List<Message> selectMessages() {
        return List.of();
    }

    @Override
    public Message updateMessage(Message message) {
        return null;
    }

    @Override
    public Message deleteMessage(UUID id) {
        return null;
    }
}
