package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.MessageAttachments;
import com.sprint.mission.discodeit.entity.MessageAttachmentsPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageAttachmentsRepository extends
    JpaRepository<MessageAttachments, MessageAttachmentsPK> {

}
