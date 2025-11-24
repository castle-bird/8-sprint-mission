package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelService implements ChannelService {
    // 채널 목록 저장을 위한 List
    private final List<Channel> channels;

    public JCFChannelService() {
        channels = new ArrayList<>();
    }


}
