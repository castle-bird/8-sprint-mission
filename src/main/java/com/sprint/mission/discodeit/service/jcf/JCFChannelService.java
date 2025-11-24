package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    // 채널 목록 저장을 위한 List
    private final List<Channel> channels;

    public JCFChannelService() {
        channels = new ArrayList<>();
    }

    @Override
    public UUID create(Channel newChannel) {
        channels.add(newChannel);

        return newChannel.getId();
    }

    @Override
    public Channel find(UUID id) {
        return channels.stream().filter(channel -> channel.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Channel> findChannels(List<UUID> ids) {
        return channels.stream().filter(channel -> ids.contains(channel.getId())).toList();
    }

    @Override
    public void update(Channel updateChannel) {
        Channel findChannels = channels.stream()
                .filter(channel -> channel.getId().equals(updateChannel.getId()))
                .findFirst()
                .orElse(null);

        if (findChannels != null) {
            findChannels.setName(updateChannel.getName());
            findChannels.setDescription(updateChannel.getDescription());
            findChannels.setUpdatedAt(System.currentTimeMillis());
            findChannels.setOwner(updateChannel.getOwner());
        } else {
            System.out.println("입력하신 채널이 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        Channel findChannels = channels.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst()
                .orElse(null);

        if ( findChannels != null ) {
            channels.remove(findChannels);
        }else {
            System.out.println("입력하신 채널이 없습니다.");
        }
    }
}
