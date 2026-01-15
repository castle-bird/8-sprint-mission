package com.sprint.mission.discodeit.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message_attachments")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MessageAttachments {

  @EmbeddedId
  private MessageAttachmentsPK id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("messageId") // PK의 messageId 필드와 매핑
  @JoinColumn(name = "message_id")
  private Message message;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("attachmentId") // PK의 attachmentId 필드와 매핑
  @JoinColumn(name = "attachment_id")
  private BinaryContent attachment;

  @Builder
  public MessageAttachments(
      Message message,
      BinaryContent attachment
  ) {
    this.id = MessageAttachmentsPK.builder()
        .messageId(message.getId())
        .attachmentId(attachment.getId())
        .build();
    this.message = message;
    this.attachment = attachment;
  }
}