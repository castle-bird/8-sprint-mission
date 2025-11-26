package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class MessageDTO {
    private UUID id;
    private String content;
    private UUID userId;
    private UUID channelId;

    public MessageDTO(UUID id ,String content, UUID userId, UUID channelId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public void setChannelId(UUID channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "MessageDTO [id=" + id + ", content=" + content + ", userId=" + userId + ", channelId=" + channelId + "]";
    }
}
