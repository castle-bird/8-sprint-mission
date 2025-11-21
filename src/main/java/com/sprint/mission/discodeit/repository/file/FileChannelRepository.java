package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileChannelRepository
        extends AbstractFileRepository<Channel>
        implements ChannelRepository {

    private static FileChannelRepository instance;

    private FileChannelRepository() {
        super(Path.of("data/channels/"));
    }

    public static synchronized FileChannelRepository getInstance() {
        if (instance == null) {
            instance = new FileChannelRepository();
        }
        return instance;
    }

    @Override
    protected UUID getId(Channel result) {
        return result.getId();
    }

    // 생성
    @Override
    public Channel create(Channel newChannel) {

        data.put(newChannel.getId(), newChannel);
        objectToFile(newChannel);

        return newChannel;
    }

    // 조회 [단건]
    @Override
    public Optional<Channel> findById(UUID id) {

        return Optional.ofNullable(data.get(id));
    }

    // 조회 [다건]
    @Override
    public List<Channel> findAll() {

        return new ArrayList<>(data.values());
    }

    // 수정
    @Override
    public Channel modify(Channel updatedChannel) {

        data.put(updatedChannel.getId(), updatedChannel);
        objectToFile(updatedChannel);

        return updatedChannel;
    }

    // 삭제
    @Override
    public void deleteById(UUID id) {

        data.remove(id);
        deleteFile(id);
    }
}
