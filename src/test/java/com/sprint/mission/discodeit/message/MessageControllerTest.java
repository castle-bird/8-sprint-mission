package com.sprint.mission.discodeit.message;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
@DisplayName("MessageController 단위 테스트")
public class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MessageService messageService;

  // 공통 상수로 뽑음 - AI추천으로 사용해봄
  private static final String TEST_MESSAGE_CONTENT = "메세지 입니다.";
  private static final String TEST_UPDATED_CONTENT = "메세지 입니다. - 수정";
  private static final String TEST_USERNAME = "Rex";
  private static final String TEST_EMAIL = "rex@gmail.com";

  private static final UUID TEST_CHANNEL_ID = UUID.randomUUID();
  private static final UUID TEST_AUTHOR_ID = UUID.randomUUID();
  private static final UUID TEST_MESSAGE_ID = UUID.randomUUID();

  @Test
  @DisplayName("성공: 첨부파일이 없어도 메세지 생성이 된다.")
  void create_message_without_attachments() throws Exception {
    // Given
    MessageCreateRequest request = new MessageCreateRequest(TEST_MESSAGE_CONTENT, TEST_CHANNEL_ID,
        TEST_AUTHOR_ID);
    UserDto authorDto = new UserDto(TEST_AUTHOR_ID, TEST_USERNAME, TEST_EMAIL, null, true);

    MessageDto responseDto = new MessageDto(
        TEST_MESSAGE_ID,
        Instant.now(),
        Instant.now(),
        TEST_MESSAGE_CONTENT,
        TEST_CHANNEL_ID,
        authorDto,
        List.of()
    );

    // Multipart
    MockMultipartFile requestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    given(
        messageService.create(any(MessageCreateRequest.class), any())
    ).willReturn(responseDto);

    // When & Then
    mockMvc.perform(multipart("/api/messages")
            .file(requestPart))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value(TEST_MESSAGE_CONTENT))
        .andExpect(jsonPath("$.author.username").value(TEST_USERNAME))
        .andExpect(jsonPath("$.channelId").value(TEST_CHANNEL_ID.toString()));
  }

  @Test
  @DisplayName("성공: 첨부파일이 있어도 메세지 생성이 된다.")
  void create_message_with_attachments() throws Exception {
    // Given
    MessageCreateRequest request = new MessageCreateRequest(TEST_MESSAGE_CONTENT, TEST_CHANNEL_ID,
        TEST_AUTHOR_ID);
    UserDto authorDto = new UserDto(TEST_AUTHOR_ID, TEST_USERNAME, TEST_EMAIL, null, true);

    MessageDto responseDto = new MessageDto(
        TEST_MESSAGE_ID,
        Instant.now(),
        Instant.now(),
        TEST_MESSAGE_CONTENT,
        TEST_CHANNEL_ID,
        authorDto,
        List.of() // 테스트 편의상 첨부파일 결과는 빈 리스트로 가정 (실제로는 BinaryContentDto 포함됨)
    );

    MockMultipartFile requestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    MockMultipartFile filePart = new MockMultipartFile(
        "attachments",
        "rex-image.png",
        MediaType.IMAGE_PNG_VALUE,
        new byte[10]
    );

    given(
        messageService.create(any(MessageCreateRequest.class), any())
    ).willReturn(responseDto);

    // When & Then
    mockMvc.perform(multipart("/api/messages")
            .file(requestPart)
            .file(filePart))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value(TEST_MESSAGE_CONTENT));
  }

  @Test
  @DisplayName("성공: 메세지 수정 ")
  void update_message() throws Exception {
    // Given
    MessageUpdateRequest request = new MessageUpdateRequest(TEST_UPDATED_CONTENT);
    UserDto authorDto = new UserDto(TEST_AUTHOR_ID, TEST_USERNAME, TEST_EMAIL, null, true);

    MessageDto responseDto = new MessageDto(
        TEST_MESSAGE_ID,
        Instant.now(),
        Instant.now(),
        TEST_UPDATED_CONTENT,
        TEST_CHANNEL_ID,
        authorDto,
        List.of()
    );

    given(
        messageService.update(eq(TEST_MESSAGE_ID), any(MessageUpdateRequest.class))
    ).willReturn(responseDto);

    // When & Then
    mockMvc.perform(patch("/api/messages/{messageId}", TEST_MESSAGE_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value(TEST_UPDATED_CONTENT));
  }


}
