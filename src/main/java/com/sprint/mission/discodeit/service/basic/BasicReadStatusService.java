package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  @Transactional
  public ReadStatusDto create(ReadStatusCreateRequest request) {

    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException("User not found"));
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new NoSuchElementException("Channel not found"));

    // 중복체크
    ReadStatus findReadStatus = readStatusRepository.findByUserIdAndChannelId(user.getId(),
        channel.getId()).orElse(null);

    Instant lastReadAt = request.lastReadAt();

    // 있으면 업데이트만
    if (findReadStatus != null) {
      return update(
          findReadStatus.getId(),
          ReadStatusUpdateRequest.builder()
              .newLastReadAt(lastReadAt)
              .build()
      );
    }

    // 없으면 생성
    ReadStatus readStatus = ReadStatus.builder()
        .user(user)
        .channel(channel)
        .lastReadAt(lastReadAt)
        .build();

    return toDto(readStatusRepository.save(readStatus));
  }

  @Override
  public ReadStatusDto find(UUID readStatusId) {
    // 찾기
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
    return toDto(readStatus);
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    // 유저 아이디로 다 찾기
    // 즉, 유저가 참여한 모든 방 찾기
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  @Transactional
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    // 수정 - 각 채널별 읽은 시간 최신화

    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException("ReadStatus not found"));

    readStatus.update(request.newLastReadAt());

    return toDto(readStatus); // save를 명시적으로 안 해도 @Transactional에 의해 저장됨
  }

  @Override
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new NoSuchElementException("ReadStatus with id " + readStatusId + " not found");
    }
    readStatusRepository.deleteById(readStatusId);
  }

  private ReadStatusDto toDto(ReadStatus readStatus) {
    return ReadStatusDto.builder()
        .id(readStatus.getId())
        .lastReadAt(readStatus.getLastReadAt())
        .build();
  }
}
