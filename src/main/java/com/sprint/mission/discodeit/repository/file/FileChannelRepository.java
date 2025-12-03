package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileChannelRepository extends AbstractFileRepository<Channel> implements ChannelRepository {
    private static FileChannelRepository instance;

    private FileChannelRepository() {
        super(Path.of("data/channels/channel.ser"));
    }

    public static synchronized FileChannelRepository getInstance() {
        if (instance == null) {
            instance = new FileChannelRepository();
        }
        return instance;
    }

    @Override
    public Channel create(Channel newChannel) {
        data.put(newChannel.getId(), newChannel);

        saveFile();
        return newChannel;
    }

    @Override
    public Channel findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel modify(Channel updatedChannel) {
        data.put(updatedChannel.getId(), updatedChannel);

        saveFile();
        return updatedChannel;
    }

    @Override
    public Channel deleteById(UUID id) {
        Channel findChannel = data.get(id);
        data.remove(id);

        saveFile();
        return findChannel;
    }
}
