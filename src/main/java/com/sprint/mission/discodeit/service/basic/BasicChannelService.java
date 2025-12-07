package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    // 생성
    @Override
    public Channel create(Channel newChannel) {

        Optional<Channel> findChannel = channelRepository.findById(newChannel.getId());

        if (findChannel.isPresent()) {
            return null;
        }

        return channelRepository.create(newChannel);
    }

    // 조회 [단건]
    @Override
    public Channel findById(UUID id) {
        return channelRepository.findById(id).orElse(null);
    }

    // 조회 [다건]
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

    // 수정
    @Override
    public Channel modify(UUID id, ChannelDTO updatedChannel) {
        Optional<Channel> findChannelOpt = channelRepository.findById(id);

        if (findChannelOpt.isEmpty()) {
            return null;
        }

        Channel findChannel = findChannelOpt.get();

        findChannel.update(
                updatedChannel.getName(),
                updatedChannel.getDescription()
        );

        return channelRepository.modify(findChannel);
    }

    // 삭제
    @Override
    public void deleteById(UUID id) {
        
        Optional<Channel> findChannelOpt = channelRepository.findById(id);

        if (findChannelOpt.isPresent()) {
            channelRepository.deleteById(id);
        }
    }
}
