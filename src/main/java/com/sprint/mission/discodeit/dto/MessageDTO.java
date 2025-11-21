package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class MessageDTO {
    private String content;
    private UUID userId;
    private UUID channelId;

    public MessageDTO(String content) {
        this.content = content;
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

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public void setChannelId(UUID channelId) {
        this.channelId = channelId;
    }
}
