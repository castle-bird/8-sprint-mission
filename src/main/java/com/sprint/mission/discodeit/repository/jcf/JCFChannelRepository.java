package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    // 싱글턴 패턴으로 데이터 공유
    // Repository에선 CRUD작업에 관한 작업 중, 데이터 유/무 체크과정 등등을 하지 않는다.
    // Service단에서 데이터 유/무 체크 관련한 작업을 한다.

    private static JCFChannelRepository instance;
    private final Map<UUID, Channel> data = new HashMap<>();

    private JCFChannelRepository() {
        // 기본생성자 private로 객체 내부에서만 사용가능하게
    }

    public static synchronized JCFChannelRepository getInstance() {
        if (instance == null) {
            instance = new JCFChannelRepository();
        }
        return instance;
    }

    @Override
    public Channel create(Channel newChannel) {
        data.put(newChannel.getId(), newChannel);
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
        return updatedChannel;
    }

    @Override
    public Channel deleteById(UUID id) {
        Channel findChannel = data.get(id);
        data.remove(id);
        return findChannel;
    }
}
