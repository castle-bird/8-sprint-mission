package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {

    // 공통
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    // 유저 정보
    private String name;
    private String password;

    public User(UUID id, long createdAt, String name, String password) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", name=" + name
                + ", password=" + password + "]";
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

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
