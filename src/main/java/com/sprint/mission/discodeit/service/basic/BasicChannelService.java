package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel save(Channel newChannel) {
        Channel findChannel = channelRepository.findById(newChannel.getId());

        if (findChannel != null) {
            return null;
        }

        return channelRepository.save(newChannel);
    }

    @Override
    public Channel findById(UUID id) {
        Channel findChannel = channelRepository.findById(id);

        if (findChannel == null) {
            return null;
        }

        return findChannel;
    }

    @Override
    public List<Channel> findAll() {
        List<Channel> allChannels = channelRepository.findAll();

        if (allChannels.isEmpty()) {
            return null;
        }

        return allChannels.stream()
                .sorted(Comparator.comparing(Channel::getName))
                .toList();
    }

    @Override
    public Channel modify(Channel updatedChannel) {
        Channel findChannel = channelRepository.findById(updatedChannel.getId());

        if (findChannel == null) {
            return null;
        }

        findChannel.update(
                updatedChannel.getName(),
                updatedChannel.getDescription()
        );

        return channelRepository.modify(findChannel);
    }

    @Override
    public Channel deleteById(UUID id) {
        Channel findChannel = channelRepository.findById(id);

        if (findChannel == null) {
            return null;
        }

        return channelRepository.deleteById(id);
    }
}
