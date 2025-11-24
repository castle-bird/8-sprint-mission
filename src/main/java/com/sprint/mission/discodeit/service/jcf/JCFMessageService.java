package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;

public class JCFMessageService implements MessageService {
    // 메세지 목록 저장을 위한 List
    private final List<Message> messages;

    public JCFMessageService() {
        messages = new ArrayList<>();
    }

}
