package com.sprint.mission.discodeit.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@DisplayName("MessageRepository 단위 테스트")
//@Transactional: @DataJpaTest에 이미 있어서 안써도 됨 
public class MessageRepositoryTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private MessageRepository messageRepository;

  private User user;
  private Channel channel;

  @BeforeEach
  void setUp() {
    user = new User("Rex", "rex@gmil.com", "12345678", null);
    userRepository.save(user);

    channel = new Channel(ChannelType.PUBLIC, "Rex 채팅방", "Rex 채팅방 입니다.");
    channelRepository.save(channel);
  }


  @Test
  @DisplayName("성공: 실제 DB에 Message가 저장된다.")
  void create_user() {
    // Given
    Message message = new Message("메세지 입니다.", channel, user, List.of());

    // When
    Message savedMessage = messageRepository.save(message);

    // Then
    assertEquals(message.getContent(), savedMessage.getContent());
    assertNotNull(message.getId());
    assertNotNull(savedMessage.getId());
    assertEquals(message.getId(), savedMessage.getId());
  }

  @Test
  @DisplayName("성공: 실제 DB에 있는 Message가 수정된다.")
  void update_user() {
    // Given
    Message message = new Message("메세지 입니다.", channel, user, List.of());
    messageRepository.save(message);

    // When
    Message findMessage = messageRepository.findById(message.getId())
        .orElseThrow(() -> new MessageNotFoundException(Map.of("requestedId", message.getId())));

    findMessage.update("메세지 입니다. - 수정");

    // Auditing 트리거
    // 위에는 1차캐시에서 수정한거라 DB반영이 아직 안됨
    messageRepository.saveAndFlush(message);

    // Then
    assertEquals(message.getContent(), findMessage.getContent());
    assertNotEquals(findMessage.getCreatedAt(), findMessage.getUpdatedAt());
  }
}
