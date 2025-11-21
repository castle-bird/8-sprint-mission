package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    // 싱글턴 패턴으로 데이터 공유
    // Repository에선 CRUD작업에 관한 작업 중, 데이터 유/무 체크과정 등등을 하지 않는다.
    // Service단에서 데이터 유/무 체크 관련한 작업을 한다.

    private static JCFUserRepository instance;
    private final Map<UUID, User> data = new HashMap<>();

    private JCFUserRepository() {
        // 기본생성자 private로 객체 내부에서만 사용가능하게
    }

    public static synchronized JCFUserRepository getInstance() {
        if (instance == null) {
            instance = new JCFUserRepository();
        }
        return instance;
    }

    // 생성
    @Override
    public User create(User newUser) {

        data.put(newUser.getId(), newUser);
        return newUser;
    }

    // 조회 [단건]
    @Override
    public Optional<User> findById(UUID id) {

        return Optional.ofNullable(data.get(id));
    }

    // 조회 [다건]
    @Override
    public List<User> findAll() {

        return new ArrayList<>(data.values());
    }

    // 수정
    @Override
    public User modify(User updatedUser) {

        data.put(updatedUser.getId(), updatedUser);

        return updatedUser;
    }

    // 삭제
    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
