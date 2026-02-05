package com.sprint.mission.discodeit.channel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
@DisplayName("ChannelController 단위 테스트")
public class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ChannelService channelService;

  @Test
  @DisplayName("성공: 공개 채팅방 생성 요청")
  void create_public_channel() throws Exception {
    // Given
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("Rex 채팅방", "Rex 채팅방 입니다.");
    ChannelDto responseDto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PUBLIC,
        "Rex 채팅방",
        "Rex 채팅방 입니다.",
        List.of(),
        Instant.now()
    );

    given(
        channelService.create(any(PublicChannelCreateRequest.class))
    ).willReturn(responseDto);

    // When & Then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Rex 채팅방"))
        .andExpect(jsonPath("$.description").value("Rex 채팅방 입니다."))
        .andExpect(jsonPath("$.type").value("PUBLIC"));
  }

  @Test
  @DisplayName("성공: 비공개 채팅방 생성 요청")
  void create_private_channel() throws Exception {
    // Given
    UUID user1Id = UUID.randomUUID();
    UUID user2Id = UUID.randomUUID();
    List<UUID> participantIds = List.of(user1Id, user2Id);

    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);

    // UserDto 생성
    UserDto user1 = new UserDto(user1Id, "Rex", "rex@test.com", null, true);
    UserDto user2 = new UserDto(user2Id, "Jessie", "jessie@test.com", null, false);

    ChannelDto responseDto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PRIVATE,
        null,
        null,
        List.of(user1, user2),
        Instant.now()
    );

    given(
        channelService.create(any(PrivateChannelCreateRequest.class))
    ).willReturn(responseDto);

    // When & Then
    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.type").value("PRIVATE"))
        .andExpect(jsonPath("$.participants").isArray()) // participants 필드 확인
        .andExpect(jsonPath("$.participants[0].username").value("Rex"));
  }

  @Test
  @DisplayName("성공: 공개 채팅방 수정 요청")
  void update_public_channel() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("Rex 채팅방 - 수정",
        "Rex 채팅방 입니다. - 수정");
    ChannelDto responseDto = new ChannelDto(
        channelId,
        ChannelType.PUBLIC,
        "Rex 채팅방 - 수정",
        "Rex 채팅방 입니다. - 수정",
        List.of(),
        Instant.now()
    );

    given(
        channelService.update(eq(channelId), any(PublicChannelUpdateRequest.class))
    ).willReturn(responseDto);

    // When & Then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Rex 채팅방 - 수정"))
        .andExpect(jsonPath("$.description").value("Rex 채팅방 입니다. - 수정"));
  }
}
