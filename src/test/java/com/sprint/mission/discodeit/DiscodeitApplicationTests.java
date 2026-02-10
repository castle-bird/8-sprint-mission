package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.impl.BasicBinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Discodeit 통합 테스트")
class DiscodeitApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private BasicBinaryContentService basicBinaryContentService;

  @MockitoBean
  private BinaryContentStorage binaryContentStorage;

  // 테스트ID
  private UUID testUserId;
  private UUID testChannelId;
  private UUID testMessageId;

  @BeforeEach
  void setUp() throws Exception {
    // 테스트용 유저 생성
    UserCreateRequest userRequest = new UserCreateRequest(
        "Rex",
        "rex@gmail.com",
        "12345678"
    );
    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(userRequest)
    );
    MockMultipartFile profilePart = new MockMultipartFile(
        "profile",
        "rex-profile.png",
        MediaType.IMAGE_PNG_VALUE,
        "test-image-content".getBytes()
    );

    MvcResult userResult = mockMvc.perform(multipart("/api/users")
            .file(userPart)
            .file(profilePart))
        .andExpect(status().isCreated())
        .andReturn();

    String userResponse = userResult.getResponse().getContentAsString();
    UserDto res = objectMapper.readValue(userResponse, UserDto.class);
    this.testUserId = res.id();

    // 테스트용 공개 채널 생성
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest(
        "Rex 채팅방",
        "Rex 채팅방 입니다.");

    MvcResult channelResult = mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(channelRequest)))
        .andExpect(status().isCreated())
        .andReturn();

    String channelResponse = channelResult.getResponse().getContentAsString();
    ChannelDto channelDto = objectMapper.readValue(channelResponse, ChannelDto.class);
    this.testChannelId = channelDto.id();
  }

  @Nested
  @DisplayName("User API 통합 테스트")
  class UserApiTest {

    @Test
    @DisplayName("유저 정보 전체 수정 및 프로필 이미지 변경")
    void update_and_get_user() throws Exception {
      UserUpdateRequest updateRequest = new UserUpdateRequest("Rex - 수정", "updated@gmail.com",
          "newpassword123");

      MockMultipartFile updatePart = new MockMultipartFile(
          "userUpdateRequest",
          "",
          MediaType.APPLICATION_JSON_VALUE,
          objectMapper.writeValueAsBytes(updateRequest)
      );

      MockMultipartFile newProfilePart = new MockMultipartFile(
          "profile",
          "new-profile.png",
          MediaType.IMAGE_PNG_VALUE,
          "new-image-content".getBytes()
      );

      mockMvc.perform(multipart("/api/users/{userId}", testUserId)
              .file(updatePart)
              .file(newProfilePart)
              .with(request -> {
                request.setMethod("PATCH");
                return request;
              }))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.username").value("Rex - 수정"))
          .andExpect(jsonPath("$.email").value("updated@gmail.com"));
    }

    @Test
    @DisplayName("유저 삭제")
    void delete_user() throws Exception {
      mockMvc.perform(delete("/api/users/{userId}", testUserId))
          .andExpect(status().isNoContent());

      assertThat(userRepository.findById(testUserId)).isEmpty();
    }
  }

  @Nested
  @DisplayName("Channel API 통합 테스트")
  class ChannelApiTest {

    @Test
    @DisplayName("비공개 채널 생성 및 조회")
    void create_private_channel() throws Exception {
      // 새로운 유저 생성 (참가자용)
      UserCreateRequest user2Request = new UserCreateRequest("Jessie", "jessie@gmail.com",
          "password");
      String user2Res = mockMvc.perform(multipart("/api/users")
              .file(new MockMultipartFile("userCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
                  objectMapper.writeValueAsBytes(user2Request)))
              .file(new MockMultipartFile("profile", "", MediaType.IMAGE_PNG_VALUE, new byte[0])))
          .andReturn().getResponse().getContentAsString();
      UUID user2Id = objectMapper.readValue(user2Res, UserDto.class).id();

      PrivateChannelCreateRequest privateRequest = new PrivateChannelCreateRequest(
          List.of(testUserId, user2Id));

      mockMvc.perform(post("/api/channels/private")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(privateRequest)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.type").value("PRIVATE"))
          .andExpect(jsonPath("$.participants.length()").value(2));
    }

    @Test
    @DisplayName("공개 채널 정보 수정")
    void update_channel() throws Exception {
      PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest("수정된 방", "수정된 설명");

      mockMvc.perform(patch("/api/channels/{channelId}", testChannelId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(updateRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value("수정된 방"));
    }

    @Test
    @DisplayName("채널 삭제")
    void delete_channel() throws Exception {
      mockMvc.perform(delete("/api/channels/{channelId}", testChannelId))
          .andExpect(status().isNoContent());

      assertThat(channelRepository.findById(testChannelId)).isEmpty();
    }
  }

  @Nested
  @DisplayName("Message API 통합 테스트")
  class MessageApiTest {

    @BeforeEach
    void createMessage() throws Exception {
      MessageCreateRequest request = new MessageCreateRequest("메세지 입니다.", testChannelId,
          testUserId);
      MockMultipartFile requestPart = new MockMultipartFile(
          "messageCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
          objectMapper.writeValueAsBytes(request)
      );

      MvcResult result = mockMvc.perform(multipart("/api/messages")
              .file(requestPart))
          .andExpect(status().isCreated())
          .andReturn();

      String response = result.getResponse().getContentAsString();
      testMessageId = UUID.fromString(objectMapper.readTree(response).get("id").asText());
    }

    @Test
    @DisplayName("첨부파일 포함 메시지 생성 및 목록 조회")
    void create_message_with_files_and_list() throws Exception {
      MessageCreateRequest request = new MessageCreateRequest("파일첨부 메시지", testChannelId,
          testUserId);
      MockMultipartFile requestPart = new MockMultipartFile("messageCreateRequest", "",
          MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request));
      MockMultipartFile file1 = new MockMultipartFile("attachments", "file1.png",
          MediaType.IMAGE_PNG_VALUE, "data1".getBytes());
      MockMultipartFile file2 = new MockMultipartFile("attachments", "file2.txt",
          MediaType.TEXT_PLAIN_VALUE, "data2".getBytes());

      mockMvc.perform(multipart("/api/messages")
              .file(requestPart)
              .file(file1)
              .file(file2))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.content").value("파일첨부 메시지"));

      mockMvc.perform(get("/api/messages")
              .param("channelId", testChannelId.toString()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @DisplayName("메시지 수정")
    void update_message() throws Exception {
      MessageUpdateRequest updateRequest = new MessageUpdateRequest("수정된 메시지 내용");
      mockMvc.perform(patch("/api/messages/{messageId}", testMessageId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(updateRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").value("수정된 메시지 내용"));
    }

    @Test
    @DisplayName("메시지 삭제")
    void delete_message() throws Exception {
      mockMvc.perform(delete("/api/messages/{messageId}", testMessageId))
          .andExpect(status().isNoContent());

      assertThat(messageRepository.findById(testMessageId)).isEmpty();
    }
  }
}