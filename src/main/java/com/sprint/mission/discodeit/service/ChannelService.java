package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // 등록
    UUID create(Channel newChannel);

    // 조회[단건]
    Channel find(UUID id);

    // 조회[다건]
    List<Channel> findChannels(List<UUID> ids);

    // 수정
    void update(Channel updateChannel);

    // 삭제
    void delete(UUID id);
}
