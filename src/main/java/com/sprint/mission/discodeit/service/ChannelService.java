package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(Channel newChannel);

    Channel findById(UUID id);

    List<Channel> findAll();

    Channel modify(Channel updatedChannel);

    Channel deleteById(UUID id);
}
