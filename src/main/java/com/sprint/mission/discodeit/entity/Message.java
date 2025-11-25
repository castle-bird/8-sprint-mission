package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends DefaultEntity{

    private String content;
    private UUID userId;
    private UUID channelId;

    public Message(String content, UUID userId, UUID channelId) {
        super();
        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
    }

    // Getter
    public String getContent() {
        return content;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    // Setter
    public void setContent(String content) {
        this.content = content;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setChannelId(UUID channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "Message [content=" + content + ", userId=" + userId + ", channelId=" + channelId + "]";
    }

}
