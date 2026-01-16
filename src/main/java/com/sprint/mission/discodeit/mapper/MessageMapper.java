package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachments;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageAttachmentsRepository;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = "spring",
    uses = {
        UserMapper.class,
        BinaryContentMapper.class
    }
)
public abstract class MessageMapper {

  private MessageAttachmentsRepository messageAttachmentsRepository;

  @Autowired
  public void set(MessageAttachmentsRepository messageAttachmentsRepository) {
    this.messageAttachmentsRepository = messageAttachmentsRepository;
  }

  // 메세지 DTO 리턴
  @Mapping(target = "author", expression = "java(getUserDto(message.getAuthor()))")
  @Mapping(target = "attachments", expression = "java(getBinaryContentDto(message))")
  @Mapping(target = "channelId", source = "channel.id")
  public abstract MessageDto toMessageDto(Message message);


  // 유저 DTO 반환
  @Named("getUserDto")
  protected abstract UserDto getUserDto(User user);

  // 첨부파일
  @Named("getBinaryContentDto")
  protected List<BinaryContentDto> getBinaryContentDto(Message message) {

    List<MessageAttachments> attachments = messageAttachmentsRepository.findByIdMessageId(
        message.getId());

    // 2. 중간 엔티티에서 실제 BinaryContent를 꺼내 DTO로 변환
    return attachments.stream()
        .map(MessageAttachments::getAttachment)
        .map(
            this::toBinaryContentDto)
        .toList();
  }


  @Named("toBinaryContentDto")
  public abstract BinaryContentDto toBinaryContentDto(BinaryContent binaryContent);
}
