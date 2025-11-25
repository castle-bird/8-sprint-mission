package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public abstract class DefaultEntity {

    // 공통
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    protected DefaultEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = 0L; // 생성시에는 업데이트 전이라 0을 넣었음
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    // ID와 생성시간은 변경하면 안되기에 업데이날짜만 setter로 받음
    // 상속받은 객체만 수정 가능하게 protected 사용
    protected void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
