package com.sprint.mission.discodeit.service.impl;

import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChannelServiceImpl implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final ChannelMapper channelMapper;

  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    // 공개 채팅방 생성

    String name = request.name();
    String description = request.description();

    Channel channel = Channel.builder()
        .type(ChannelType.PUBLIC)
        .name(name)
        .description(description)
        .build();

    return channelMapper.toChannelDto(channelRepository.save(channel));
  }

  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    // 비공개 채팅방 생성 1:1

    Channel channel = Channel.builder()
        .type(ChannelType.PRIVATE)
        .name("")
        .description("")
        .build();

    Channel createdChannel = channelRepository.save(channel);

    // 참여자 목록 돌면서 ReadStatus 작성
    request.participantIds().stream()
        .map(userId -> {
          User user = userRepository.findById(userId)
              .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

          return ReadStatus.builder()
              .user(user)
              .channel(createdChannel)
              .lastReadAt(Instant.EPOCH)
              .build();
        })
        .forEach(readStatusRepository::save);

    return channelMapper.toChannelDto(createdChannel);
  }

  @Override
  public ChannelDto find(UUID channelId) {
    // 채널 찾기
    return channelRepository.findById(channelId)
        .map(channelMapper::toChannelDto)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    // 특정 유저가 참여 가능한 채팅방 다 찾기
    // PUBLIC 기본 + PRIVATE

    // 읽은 목록(ReadStatus)로 이미 참여한 방 찾기
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();

    // PUBLIC전체 + PRIVATE 추가
    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC)
            || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(channelMapper::toChannelDto)
        .toList();
  }

  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    // 수정

    // 체널 체크
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));

    // 비공개방이면 수정 불가
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new IllegalArgumentException("Private channel cannot be updated");
    }

    String newName = request.newName();
    String newDescription = request.newDescription();

    channel.update(newName, newDescription);

    return channelMapper.toChannelDto(channelRepository.save(channel));
  }

  @Override
  public void delete(UUID channelId) {
    // 삭제

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));

    channelRepository.deleteById(channelId);
  }
}
