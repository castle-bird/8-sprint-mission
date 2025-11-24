package com.sprint.mission.discodeit.entity;

import java.util.List;
import java.util.UUID;

public class User {

    // 공통
    private UUID id;
    private long createdAt;
    private long updatedAt;

    // 유저 정보
    private String name;
    private String password;
    private List<Message> messages;
    private List<Channel> channels;

    @Override
    public String toString() {
        return "User [id=" + id + ", " +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", name=" + name +
                ", password=" + password +
                ", messages=" + messages +
                ", channels=" + channels + "]";
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

    public List<Message> getMessages() {
        return messages;
    }

    public List<Channel> getChannels() {
        return channels;
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

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }
}
