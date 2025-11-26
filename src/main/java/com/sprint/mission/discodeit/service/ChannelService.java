package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDTO save(Channel newChannel);

    ChannelDTO findById(UUID id);

    List<ChannelDTO> findAll();

    ChannelDTO modify(Channel updatedChannel);

    ChannelDTO deleteById(UUID id);

    // DTO -> Entity 매핑
    default ChannelDTO entityToDto(Channel channel) {
        return new ChannelDTO(
                channel.getId(),
                channel.getName(),
                channel.getDescription()
        );
    }
}
