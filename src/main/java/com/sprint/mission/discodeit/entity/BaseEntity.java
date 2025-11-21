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
        this.updatedAt = System.currentTimeMillis();
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

    protected void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
