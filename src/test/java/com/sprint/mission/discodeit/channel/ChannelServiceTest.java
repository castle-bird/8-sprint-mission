package com.sprint.mission.discodeit.channel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelModifyException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.impl.BasicChannelService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChannelService 단위 테스트")
public class ChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ChannelMapper channelMapper;

  // 테스트 대상
  @InjectMocks
  private BasicChannelService channelService;

  @Nested
  @DisplayName("채널 생성 테스트")
  class channel_create {

    private PublicChannelCreateRequest publicChannelCreateRequest;
    private PrivateChannelCreateRequest privateChannelCreateRequest;

    @BeforeEach
    void setUp() {
      publicChannelCreateRequest = new PublicChannelCreateRequest("Rex 채팅방", "Rex가 만든 채팅방입니다.");
      privateChannelCreateRequest = new PrivateChannelCreateRequest(
          List.of(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    @DisplayName("성공: 공개 채널 생성이 된다.")
    void create_public_channel() {
      // Given
      ChannelDto expectedDto = new ChannelDto(
          UUID.randomUUID(),
          ChannelType.PUBLIC,
          publicChannelCreateRequest.name(),
          publicChannelCreateRequest.description(),
          List.of(),
          Instant.now()
      );

      given(
          channelMapper.toDto(any(Channel.class))
      ).willReturn(expectedDto);

      // When
      ChannelDto result = channelService.create(publicChannelCreateRequest);

      // Then
      then(channelRepository).should().save(any(Channel.class));
      assertEquals(publicChannelCreateRequest.name(), result.name());
    }

    @Test
    @DisplayName("성공: 비공개 채널 생성이 된다.")
    void create_private_channel() {
      // Given
      ChannelDto expectedDto = new ChannelDto(
          UUID.randomUUID(),
          ChannelType.PRIVATE,
          null,
          null,
          List.of(),
          Instant.now()
      );

      given(
          channelMapper.toDto(any(Channel.class))
      ).willReturn(expectedDto);

      given(
          userRepository.findAllById(privateChannelCreateRequest.participantIds())
      ).willReturn(List.of());

      // When
      ChannelDto result = channelService.create(privateChannelCreateRequest);

      // Then
      then(channelRepository).should().save(any(Channel.class));
      then(readStatusRepository).should().saveAll(any());
      assertEquals(expectedDto.type(), result.type());
      assertEquals(expectedDto.name(), result.name());
    }
  }

  // ================================================================================================

  @Nested
  @DisplayName("채널 수정 테스트")
  class channel_update {

    PublicChannelUpdateRequest publicChannelUpdateRequest;

    @BeforeEach
    void setUp() {
      publicChannelUpdateRequest = new PublicChannelUpdateRequest(
          "Rex 채팅방-수정",
          "Rex가 만든 채팅방입니다.-수정"
      );
    }

    @Test
    @DisplayName("성공: 공개 채널 수정이 된다.")
    void update_public_channel() {
      // Given
      ChannelDto expectedDto = new ChannelDto(
          UUID.randomUUID(),
          ChannelType.PUBLIC,
          publicChannelUpdateRequest.newName(),
          publicChannelUpdateRequest.newDescription(),
          List.of(),
          Instant.now()
      );

      Channel findChannel = new Channel(
          ChannelType.PUBLIC,
          "Rex 채팅방",
          "Rex가 만든 채팅방입니다."
      );

      given(
          channelRepository.findById(any(UUID.class))
      ).willReturn(Optional.of(findChannel));

      given(
          channelMapper.toDto(any(Channel.class))
      ).willReturn(expectedDto);

      // When
      ChannelDto result = channelService.update(UUID.randomUUID(), publicChannelUpdateRequest);

      // Then
      assertEquals(expectedDto.name(), result.name());
      assertEquals(expectedDto.description(), result.description());
    }

    @Test
    @DisplayName("성공: 비공개 채널은 수정이 안된다.")
    void update_private_channel() {
      // Given
      Channel findChannel = new Channel(
          ChannelType.PRIVATE,
          null,
          null
      );

      given(
          channelRepository.findById(any(UUID.class))
      ).willReturn(Optional.of(findChannel));

      // When & Then
      PrivateChannelModifyException e = assertThrows(PrivateChannelModifyException.class, () -> {
        channelService.update(UUID.randomUUID(), publicChannelUpdateRequest);
      });

      assertEquals("비공개 채널은 수정 불가능합니다.", e.getMessage());
    }
  }
}