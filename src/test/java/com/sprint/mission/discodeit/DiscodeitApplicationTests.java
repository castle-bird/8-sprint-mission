package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.impl.BasicBinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
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
import org.springframework.http.ResponseEntity;
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
  private ReadStatusRepository readStatusRepository;

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
        "",
        MediaType.IMAGE_PNG_VALUE,
        new byte[0]
    );

    MvcResult userResult = mockMvc.perform(multipart("/api/users")
            .file(userPart)
            .file(profilePart))
        .andExpect(status().isCreated())
        .andReturn();

    String userResponse = userResult.getResponse().getContentAsString();
    UserDto res = objectMapper.readValue(userResponse, UserDto.class);
    this.testUserId = res.id();

    // 테스트용 채널 생성
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
  @DisplayName("인증 API 통합 테스트")
  class AuthApiTest {

    @Test
    @DisplayName("성공: 로그인")
    void login() throws Exception {
      LoginRequest loginRequest = new LoginRequest("Rex", "12345678");
      mockMvc.perform(post("/api/auth/login")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(loginRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.username").value("Rex"));
    }
  }

  @Nested
  @DisplayName("유저 API 통합 테스트")
  class UserApiTest {

    @Test
    @DisplayName("성공: 유저 정보 수정, 상태 수정 및 조회")
    void user_lifecycle() throws Exception {
      // 정보 수정
      UserUpdateRequest updateRequest = new UserUpdateRequest("Rex - 수정", "new@gmail.com",
          "newpass123");
      MockMultipartFile updatePart = new MockMultipartFile(
          "userUpdateRequest", "", MediaType.APPLICATION_JSON_VALUE,
          objectMapper.writeValueAsBytes(updateRequest)
      );
      MockMultipartFile newProfile = new MockMultipartFile("profile", "new.png",
          MediaType.IMAGE_PNG_VALUE, "new profile".getBytes());

      mockMvc.perform(multipart("/api/users/{userId}", testUserId)
              .file(updatePart)
              .file(newProfile)
              .with(request -> {
                request.setMethod("PATCH");
                return request;
              }))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.username").value("Rex - 수정"));

      // 상태 수정
      UserStatusUpdateRequest statusRequest = new UserStatusUpdateRequest(Instant.now());
      mockMvc.perform(patch("/api/users/{userId}/userStatus", testUserId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(statusRequest)))
          .andExpect(status().isOk());

      // 전체 조회
      mockMvc.perform(get("/api/users"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("성공: 유저 삭제")
    void delete_user() throws Exception {
      mockMvc.perform(delete("/api/users/{userId}", testUserId))
          .andExpect(status().isNoContent());
      assertThat(userRepository.findById(testUserId)).isEmpty();
    }
  }

  @Nested
  @DisplayName("채널 API 통합 테스트")
  class ChannelApiTest {

    @Test
    @DisplayName("성공: 비공개 채널 생성, 수정 및 조회")
    void channel_lifecycle() throws Exception {
      // 비공개 채널 생성
      PrivateChannelCreateRequest privateRequest = new PrivateChannelCreateRequest(
          List.of(testUserId, UUID.randomUUID()));
      mockMvc.perform(post("/api/channels/private")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(privateRequest)))
          .andExpect(status().isCreated());

      // 수정
      PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest("수정방", "수정설명");
      mockMvc.perform(patch("/api/channels/{channelId}", testChannelId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(updateRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value("수정방"));

      // 조회
      mockMvc.perform(get("/api/channels")
              .param("userId", testUserId.toString()))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("성공: 채널 삭제")
    void delete_channel() throws Exception {
      MessageCreateRequest msgRequest = new MessageCreateRequest("삭제될 채널의 메시지", testChannelId,
          testUserId);
      MockMultipartFile requestPart = new MockMultipartFile("messageCreateRequest", "",
          MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(msgRequest));
      mockMvc.perform(multipart("/api/messages").file(requestPart)).andExpect(status().isCreated());

      mockMvc.perform(delete("/api/channels/{channelId}", testChannelId))
          .andExpect(status().isNoContent());
      assertThat(channelRepository.findById(testChannelId)).isEmpty();
    }
  }

  @Nested
  @DisplayName("메세지 API 통합 테스트")
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
    @DisplayName("성공: 메세지 수정 및 목록 조회")
    void update_and_list_messages() throws Exception {
      MessageUpdateRequest updateRequest = new MessageUpdateRequest("수정됨");
      mockMvc.perform(patch("/api/messages/{messageId}", testMessageId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(updateRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").value("수정됨"));

      mockMvc.perform(get("/api/messages")
              .param("channelId", testChannelId.toString())
              .param("cursor", Instant.now().toString())
              .param("size", "10"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("성공: 메세지 삭제")
    void delete_message() throws Exception {
      mockMvc.perform(delete("/api/messages/{messageId}", testMessageId))
          .andExpect(status().isNoContent());
      assertThat(messageRepository.findById(testMessageId)).isEmpty();
    }
  }

  @Nested
  @DisplayName("읽음 상태 API 통합 테스트")
  class ReadStatusApiTest {

    private UUID testReadStatusId;

    @BeforeEach
    void createReadStatus() throws Exception {
      ReadStatusCreateRequest request = new ReadStatusCreateRequest(testUserId, testChannelId,
          Instant.now());
      MvcResult result = mockMvc.perform(post("/api/readStatuses")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andReturn();
      testReadStatusId = UUID.fromString(
          objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText());
    }

    @Test
    @DisplayName("성공: 읽음 상태 수정 및 조회")
    void update_and_get_read_status() throws Exception {
      ReadStatusUpdateRequest updateRequest = new ReadStatusUpdateRequest(Instant.now());
      mockMvc.perform(patch("/api/readStatuses/{readStatusId}", testReadStatusId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(updateRequest)))
          .andExpect(status().isOk());

      mockMvc.perform(get("/api/readStatuses").param("userId", testUserId.toString()))
          .andExpect(status().isOk());
    }
  }

  @Nested
  @DisplayName("바이너리 컨텐츠 API 통합 테스트")
  class BinaryContentApiTest {

    private UUID testBinaryContentId;

    @BeforeEach
    void createBinaryContent() throws Exception {
      MessageCreateRequest request = new MessageCreateRequest("파일", testChannelId, testUserId);
      MockMultipartFile requestPart = new MockMultipartFile("messageCreateRequest", "",
          MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request));
      MockMultipartFile filePart = new MockMultipartFile("attachments", "test.txt", "text/plain",
          "test".getBytes());

      MvcResult result = mockMvc.perform(
              multipart("/api/messages").file(requestPart).file(filePart))
          .andExpect(status().isCreated()).andReturn();
      String response = result.getResponse().getContentAsString();
      if (!objectMapper.readTree(response).get("attachments").isEmpty()) {
        testBinaryContentId = UUID.fromString(
            objectMapper.readTree(response).get("attachments").get(0).get("id").asText());
      }
    }

    @Test
    @DisplayName("성공: 바이너리 컨텐츠 조회 및 다운로드")
    void get_and_download_binary_content() throws Exception {
      if (testBinaryContentId == null) {
        return;
      }
      mockMvc.perform(get("/api/binaryContents/{id}", testBinaryContentId))
          .andExpect(status().isOk());
      mockMvc.perform(
              get("/api/binaryContents").param("binaryContentIds", testBinaryContentId.toString()))
          .andExpect(status().isOk());
      given(binaryContentStorage.download(any(BinaryContentDto.class))).willReturn(
          ResponseEntity.ok().build());
      mockMvc.perform(get("/api/binaryContents/{id}/download", testBinaryContentId))
          .andExpect(status().isOk());
    }
  }

  @Nested
  @DisplayName("예외 상황 테스트")
  class ExceptionTest {

    @Test
    @DisplayName("실패: 중복된 이메일로 유저 생성")
    void create_user_duplicate_email() throws Exception {
      UserCreateRequest duplicateRequest = new UserCreateRequest("NewUser", "rex@gmail.com",
          "password");
      MockMultipartFile userPart = new MockMultipartFile("userCreateRequest", "",
          MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(duplicateRequest));

      mockMvc.perform(multipart("/api/users").file(userPart))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("실패: 중복된 이름으로 유저 생성")
    void create_user_duplicate_name() throws Exception {
      UserCreateRequest duplicateRequest = new UserCreateRequest("Rex", "newEmail@gmail.com",
          "password");
      MockMultipartFile userPart = new MockMultipartFile("userCreateRequest", "",
          MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(duplicateRequest));

      mockMvc.perform(multipart("/api/users").file(userPart))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("실패: 존재하지 않는 유저 삭제")
    void delete_non_existent_user() throws Exception {
      mockMvc.perform(delete("/api/users/{userId}", UUID.randomUUID()))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("실패: 존재하지 않는 채널 삭제")
    void delete_non_existent_channel() throws Exception {
      mockMvc.perform(delete("/api/channels/{channelId}", UUID.randomUUID()))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("실패: 비공개 채팅방 수정")
    void update_private_channel() throws Exception {
      // 비공개 채널 생성
      PrivateChannelCreateRequest privateRequest = new PrivateChannelCreateRequest(
          List.of(testUserId, UUID.randomUUID()));

      MvcResult result = mockMvc.perform(post("/api/channels/private")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(privateRequest)))
          .andExpect(status().isCreated())
          .andReturn();

      ChannelDto privateChannel = objectMapper.readValue(result.getResponse().getContentAsString(),
          ChannelDto.class);

      // 비공개 채널 수정 시도
      PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest("수정방", "수정설명");
      mockMvc.perform(patch("/api/channels/{channelId}", privateChannel.id())
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(updateRequest)))
          .andExpect(status().isBadRequest());
    }
  }

}
