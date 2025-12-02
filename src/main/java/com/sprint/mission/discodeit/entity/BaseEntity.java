package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class BaseEntity implements Serializable {

    // 공통
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    protected BaseEntity() {
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

    /**
     * ID와 생성 시간은 불변으로 변경하지 않아야 하므로 setter를 제공하지 않았습니다.
     * 업데이트 시각(updatedAt)만 변경 가능하도록 제한하며,
     * 이 메서드는 상속받은 클래스에서만 접근할 수 있게 protected로 선언하였습니다.
     *
     * @param updatedAt 변경할 최종 수정 시각 (밀리초 단위)
     */
    protected void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
