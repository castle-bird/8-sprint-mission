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

    // 생성
    @Override
    public Channel create(Channel newChannel) {

        data.put(newChannel.getId(), newChannel);

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

        return updatedChannel;
    }

    // 삭제
    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
