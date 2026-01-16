package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ChannelMapper {

  private MessageRepository messageRepository;
  private ReadStatusRepository readStatusRepository;

  @Autowired
  public void set(MessageRepository messageRepository, ReadStatusRepository readStatusRepository) {
    this.messageRepository = messageRepository;
    this.readStatusRepository = readStatusRepository;
  }

  @Mapping(target = "lastMessageAt", expression = "java(getLastMessageAt(channel))")
  @Mapping(target = "participantIds", expression = "java(getParticipantIds(channel))")
  public abstract ChannelDto toChannelDto(Channel channel);


  // 채널의 가장 최근 메세지 조회
  @Named("getLastMessageAt")
  protected Instant getLastMessageAt(Channel channel) {

    return messageRepository.findAllByChannelId(channel.getId())
        .stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.EPOCH);
  }

  // 참여자: 비공개 채팅방만
  // 비공개 채팅방 참여자만 찾는 이유: 공개채팅방의 경우 굳이 찾을 필요가 없음. 너무 많고 오픈되어있어서
  @Named("getParticipantIds")
  protected List<UUID> getParticipantIds(Channel channel) {

    // 비공개 채팅바이 아니면 빈배열 리턴
    if (channel.getType() != ChannelType.PRIVATE) {
      return List.of();
    }

    return readStatusRepository.findAllByChannelId(channel.getId())
        .stream()
        .map(readStatus -> readStatus.getUser().getId())
        .toList();
  }
}
