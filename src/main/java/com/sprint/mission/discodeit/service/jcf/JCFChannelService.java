package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> channelsData;

    public JCFChannelService() {
        channelsData = new HashMap<>();
    }

    @Override
    public ChannelDTO save(Channel newChannel) {
        channelsData.put(newChannel.getId(), newChannel);

        return entityToDto(
                channelsData.get(newChannel.getId())
        );
    }

    @Override
    public ChannelDTO findById(UUID id) {
        Channel findChannel = channelsData.get(id);

        if (findChannel == null) {
            System.out.println("[JCFChannelService] findById(): 존재하지 않는 채널입니다.");
            // throw new RuntimeException("[JCFChannelService] findById(): 존재하지 않는 채널입니다.");

            return null;
        }

        return entityToDto(findChannel);
    }

    @Override
    public List<ChannelDTO> findAll() {
        List<Channel> allChannels = channelsData.values().stream().toList();

        if (allChannels.isEmpty()) {
            System.out.println("[JCFChannelService] findAll(): 채널이 0개 입니다.");
        }

        return allChannels.stream().map(this::entityToDto).toList();
    }

    @Override
    public ChannelDTO modify(Channel updatedChannel) {
        Channel findChannel = channelsData.get(updatedChannel.getId());

        if (findChannel == null) {
            System.out.println("[JCFChannelService] findById(): 존재하지 않는 채널입니다.");
            // throw new RuntimeException("[JCFChannelService] findById(): 존재하지 않는 채널입니다.");

            return null;
        }

        findChannel.update(
                updatedChannel.getName(),
                updatedChannel.getDescription()
        );

        return entityToDto(findChannel);
    }

    @Override
    public ChannelDTO deleteById(UUID id) {
        Channel findChannel = channelsData.get(id);

        if (findChannel == null) {
            System.out.println("[JCFChannelService] findById(): 존재하지 않는 채널입니다.");
            // throw new RuntimeException("[JCFChannelService] findById(): 존재하지 않는 채널입니다.");

            return null;
        }

        channelsData.remove(id);

        return entityToDto(findChannel);
    }
}
