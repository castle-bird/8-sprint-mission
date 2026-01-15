package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MessageAttachmentsPK implements Serializable {

  @Column(name = "message_id")
  private UUID messageId; // Message 엔티티의 ID 타입

  @Column(name = "attachment_id")
  private UUID attachmentId; // BinaryContent 엔티티의 ID 타입

  @Builder
  private MessageAttachmentsPK(UUID messageId, UUID attachmentId) {
    this.messageId = messageId;
    this.attachmentId = attachmentId;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MessageAttachmentsPK that = (MessageAttachmentsPK) o;
    return Objects.equals(messageId, that.messageId) && Objects.equals(
        attachmentId, that.attachmentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, attachmentId);
  }
}
