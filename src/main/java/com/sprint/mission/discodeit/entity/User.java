package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {

    // 공통 필드
    UUID id;
    Long createdAt;
    Long updatedAt;

    // 개별필드
    String name;
    String nickname;
    String email;

    public User(UUID id, Long createdAt, String name, String nickname, String email) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    // getter
    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    // setter
    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
