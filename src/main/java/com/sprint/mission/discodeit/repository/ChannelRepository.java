package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {

    Channel create(Channel newChannel);

    Channel findById(UUID id);

    List<Channel> findAll();

    Channel modify(Channel updatedChannel);

    Channel deleteById(UUID id);
}
