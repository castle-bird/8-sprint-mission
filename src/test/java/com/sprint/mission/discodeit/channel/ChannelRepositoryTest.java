package com.sprint.mission.discodeit.channel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@DisplayName("ChannelRepository 단위 테스트")
//@Transactional: @DataJpaTest에 이미 있어서 안써도 됨 
public class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;


  @Test
  @DisplayName("성공: 실제 DB에 Channel이 저장된다.")
  void create_user() {
    // Given
    Channel channel = new Channel(ChannelType.PUBLIC, "Rex 채팅방", "Rex 채팅방 입니다.");

    // When
    Channel savedChannel = channelRepository.save(channel);

    // Then
    assertEquals(savedChannel.getName(), channel.getName());
    assertEquals(savedChannel.getDescription(), channel.getDescription());
    assertNotNull(savedChannel.getCreatedAt());
  }

  @Test
  @DisplayName("성공: 실제 DB에 있는 Channel이 수정된다.")
  void update_user() {
    // Given
    Channel channel = new Channel(ChannelType.PUBLIC, "Rex 채팅방", "Rex 채팅방 입니다.");
    channelRepository.save(channel);

    // When
    Channel findChannel = channelRepository.findById(channel.getId())
        .orElseThrow(() -> new ChannelNotFoundException(Map.of("requestedId", channel.getId())));

    findChannel.update(
        "Rex 채팅방-수정",
        "Rex 채팅방 입니다.-수정"
    );

    // Auditing 트리거
    // 위에는 1차캐시에서 수정한거라 DB반영이 아직 안됨
    channelRepository.saveAndFlush(findChannel);

    // Then
    assertEquals("Rex 채팅방-수정", channel.getName());
    assertEquals("Rex 채팅방-수정", findChannel.getName());
    //초기 생성시 생성/수정 날짜 똑같이 생성 했는데, 수정 했으니 바뀌어야함
    assertNotEquals(findChannel.getCreatedAt(), findChannel.getUpdatedAt());
  }
}
