package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    // 공통
    private UUID id;
    private long createdAt;
    private long updatedAt;

    // 메세지 정보
    private String content;
    private User user;
    private Channel channel;

    @Override
    public String toString() {
        return "Message [id=" + id + ", " +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", content=" + content +
                ", user=" + user +
                ", channel=" + channel + "]";
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

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
