package com.sprint.mission.discodeit.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.impl.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
@DisplayName("MessageService 단위 테스트")
public class MessageServiceTest {

  @Mock
  private MessageRepository messageRepository;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private MessageMapper messageMapper;
  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private PageResponseMapper pageResponseMapper;

  // 테스트 대상
  @InjectMocks
  private BasicMessageService messageService;

  @Nested
  @DisplayName("메세지 생성 테스트")
  class message_create {

    // 유저
    private User user;
    private UserDto userDto;

    //채널
    private Channel channel;
    private MessageCreateRequest messageCreateRequest;

    @BeforeEach
    void setUp() {
      userDto = new UserDto(
          UUID.randomUUID(),
          "Rex",
          "rex@gmail.com",
          null,
          true
      );

      messageCreateRequest = new MessageCreateRequest(
          "메세지 내용입니다.",
          UUID.randomUUID(),
          UUID.randomUUID()
      );

      channel = new Channel(
          ChannelType.PUBLIC,
          "Rex가 채팅방",
          "Rex가 만든 채팅방 입니다."
      );

      user = new User(
          "Rex",
          "rex@gmail.com",
          "12345678",
          null
      );
    }

    @Test
    @DisplayName("성공: 첨부파일이 없어도 메세지 생성이 된다.")
    void create_message_without_files() {
      // Given
      MessageDto messageDto = new MessageDto(
          UUID.randomUUID(),
          Instant.now(),
          Instant.now(),
          messageCreateRequest.content(),
          messageCreateRequest.channelId(),
          userDto,
          List.of()
      );

      given(
          channelRepository.findById(any(UUID.class))
      ).willReturn(Optional.of(channel));

      given(
          userRepository.findById(any(UUID.class))
      ).willReturn(Optional.of(user));

      given(
          messageMapper.toDto(any(Message.class))
      ).willReturn(messageDto);

      // When
      MessageDto result = messageService.create(messageCreateRequest, List.of());

      // Then
      assertEquals(result.content(), messageDto.content());
      then(messageRepository).should().save(any(Message.class));
    }

    @Test
    @DisplayName("성공: 첨부파일이 있어도 메세지 생성이 된다.")
    void create_message_with_files() {
      // Given

      // 메세지 응답용
      List<BinaryContentDto> binaryContentDtos = List.of(
          new BinaryContentDto(
              UUID.randomUUID(),
              "rex1.png",
              (long) "rex-image1".getBytes().length,
              "image/png"
          ),
          new BinaryContentDto(
              UUID.randomUUID(),
              "rex2.png",
              (long) "rex-image2".getBytes().length,
              "image/png"
          ));

      MessageDto messageDto = new MessageDto(
          UUID.randomUUID(),
          Instant.now(),
          Instant.now(),
          messageCreateRequest.content(),
          messageCreateRequest.channelId(),
          userDto,
          binaryContentDtos
      );

      // 메세지 만들때 필요한 request
      List<BinaryContentCreateRequest> binaryContentCreateRequests = List.of(
          new BinaryContentCreateRequest("rex1.png", "image/png", "rex-image1".getBytes()),
          new BinaryContentCreateRequest("rex2.png", "image/png", "rex-image2".getBytes()));

      given(
          channelRepository.findById(any(UUID.class))
      ).willReturn(Optional.of(channel));

      given(
          userRepository.findById(any(UUID.class))
      ).willReturn(Optional.of(user));

      given(
          messageMapper.toDto(any(Message.class))
      ).willReturn(messageDto);

      // When
      MessageDto result = messageService.create(messageCreateRequest, binaryContentCreateRequests);

      // Then
      assertEquals(result.content(), messageDto.content());
      assertEquals(result.attachments().size(), binaryContentCreateRequests.size());
      then(messageRepository).should().save(any(Message.class));
    }
  }

  // ================================================================================================

  @Nested
  @DisplayName("메세지 수정 테스트")
  class message_update {

    // 유저
    private User user;
    private UserDto userDto;

    //채널
    private Channel channel;

    // 메세지
    private Message message;

    @BeforeEach
    void setUp() {
      channel = new Channel(
          ChannelType.PUBLIC,
          "Rex가 채팅방",
          "Rex가 만든 채팅방 입니다."
      );

      user = new User(
          "Rex",
          "rex@gmail.com",
          "12345678",
          null
      );

      message = new Message(
          "메세지 입니다.",
          channel,
          user,
          List.of()
      );

      userDto = new UserDto(
          UUID.randomUUID(),
          "Rex",
          "rex@gmail.com",
          null,
          true
      );
    }

    @Test
    @DisplayName("성공: 메세지 내용을 수정할 수 있다.")
    void update_message() {
      // Given
      MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest(
          "메세지 입니다. - 수정"
      );

      MessageDto messageDto = new MessageDto(
          UUID.randomUUID(),
          Instant.now(),
          Instant.now(),
          "메세지 입니다. - 수정",
          UUID.randomUUID(),
          userDto,
          List.of()
      );

      given(
          messageMapper.toDto(any(Message.class))
      ).willReturn(messageDto);

      given(
          messageRepository.findById(any(UUID.class))
      ).willReturn(Optional.of(message));

      // When
      MessageDto result = messageService.update(UUID.randomUUID(), messageUpdateRequest);

      // Then
      assertEquals(result.content(), messageDto.content()); // 입력값이 그대로 잘 나왔는지
      assertEquals(result.content(), message.getContent()); // 메세지 수정 됐는지
    }
  }

  @Nested
  @DisplayName("메세지 삭제 테스트")
  class message_delete {

    @Test
    @DisplayName("성공: 메세지가 삭제 된다.")
    void delete_message() {
      // Given
      UUID messageId = UUID.randomUUID();
      given(
          messageRepository.existsById(messageId)
      ).willReturn(true);

      // When
      messageService.delete(messageId);

      // Then
      then(messageRepository).should().deleteById(messageId);
    }
  }
}