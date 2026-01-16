package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.MessageAttachments;
import com.sprint.mission.discodeit.entity.MessageAttachmentsPK;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageAttachmentsRepository extends
    JpaRepository<MessageAttachments, MessageAttachmentsPK> {

  List<MessageAttachments> findByIdMessageId(UUID messageId);
}