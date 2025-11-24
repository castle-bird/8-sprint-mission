package com.sprint.mission.discodeit.entity;

import java.util.List;
import java.util.UUID;

public class Channel {
    // 공통
    private UUID id;
    private long createdAt;
    private long updatedAt;

    // 채널 내용
    private String name;
    private String description;
    private User owner;
    private List<User> users;
    private List<Message> messages;

    @Override
    public String toString() {
        return "Channel [id=" + id + ", " +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", name=" + name +
                ", description=" + description +
                ", owner=" + owner +
                ", users=" + users +
                ", messages=" + messages + "]";
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

    public String getDescription() {
        return description;
    }

    public User getOwner() {
        return owner;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
