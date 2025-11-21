package com.sprint.mission.discodeit.entity;

import java.util.List;
import java.util.UUID;

public class Channel {

    // 공통 필드
    UUID id;
    Long createdAt;
    Long updatedAt;

    // 개별필드
    String channelName;
    List<User> users;

    public Channel(UUID id, Long createdAt, String channelName, List<User> users) {
        this.id = id;
        this.createdAt = createdAt;
        this.channelName = channelName;
        this.users = users;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", channelName='" + channelName + '\'' +
                ", users='" + users + '\'' +
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

    public String getChannelName() {
        return channelName;
    }

    public List<User> getUsers() {
        return users;
    }

    // setter
    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
