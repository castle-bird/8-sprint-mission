package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachments;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageAttachmentsRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final MessageAttachmentsRepository messageAttachmentsRepository;

  @Override
  @Transactional
  public MessageDto create(
      MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests
  ) {

    // 채널과 사용자 체크
    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> new NoSuchElementException("Channel not found"));
    User author = userRepository.findById(messageCreateRequest.authorId())
        .orElseThrow(() -> new NoSuchElementException("Author not found"));

    // 메세지 생성 및 저장
    Message message = Message.builder()
        .content(messageCreateRequest.content())
        .channel(channel)
        .author(author)
        .build();
    Message savedMessage = messageRepository.save(message);

    // 첨부파일 저장 && 매핑 엔티티 일괄 생성(= MessageAttachments)
    if (binaryContentCreateRequests != null && !binaryContentCreateRequests.isEmpty()) {

      List<BinaryContent> binaryContents = binaryContentCreateRequests.stream()
          .map(req -> BinaryContent.builder()
              .fileName(req.fileName())
              .contentType(req.contentType())
              .size((long) req.bytes().length)
              .bytes(req.bytes())
              .build())
          .toList();

      // BinaryContent 일괄 저장
      List<BinaryContent> savedContents = binaryContentRepository.saveAll(binaryContents);

      List<MessageAttachments> attachments = savedContents.stream()
          .map(content -> MessageAttachments.builder()
              .message(savedMessage)
              .attachment(content)
              .build())
          .toList();

      // MessageAttachments 일괄 저장
      messageAttachmentsRepository.saveAll(attachments);
    }

    return toDto(savedMessage);
  }

  @Override
  public MessageDto find(UUID messageId) {
    // 메세지 찾기
    Message findMessage = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    return toDto(findMessage);
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    // 특정 채널 모든 메세지 찾기
    return messageRepository.findAllByChannelId(channelId).stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    // 메세지 수정
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    String newContent = request.newContent();
    message.update(newContent);

    return toDto(messageRepository.save(message));
  }

  @Override
  public void delete(UUID messageId) {
    // 메세지 삭제
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    
    messageRepository.deleteById(messageId);
  }

  private MessageDto toDto(Message message) {
    return MessageDto.builder()
        .id(message.getId())
        .content(message.getContent())
        .createdAt(message.getCreatedAt())
        .build();
  }
}
