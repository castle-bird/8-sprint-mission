package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {

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
    public void update(String content) {
        if (content != null) this.content = content;

        super.setUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Message [content=" + content + ", userId=" + userId + ", channelId=" + channelId + "]";
    }

}
