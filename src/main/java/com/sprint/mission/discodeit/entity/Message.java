package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {

    // 공통 필드
    UUID id;
    Long createdAt;
    Long updatedAt;

    // 개별필드
    String messageContents;
    User sender;
    Channel channel;

    public Message(UUID id, Long createdAt, String messageContents, User sender, Channel channel) {
        this.id = id;
        this.createdAt = createdAt;
        this.messageContents = messageContents;
        this.sender = sender;
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", messageContents='" + messageContents + '\'' +
                ", sender='" + sender + '\'' +
                ", channel='" + channel + '\'' +
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

    public String getMessageContents() {
        return messageContents;
    }

    public User getSender() {
        return sender;
    }

    public Channel getChannel() {
        return channel;
    }

    // setter
    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setMessageContents(String messageContents) {
        this.messageContents = messageContents;
    }
}
