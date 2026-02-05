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
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.impl.BasicBinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
        "",
        MediaType.IMAGE_PNG_VALUE,
        new byte[0]
    );

    MvcResult userResult = mockMvc.perform(multipart("/api/users")
            .file(userPart)
            .file(profilePart))
        .andExpect(status().isCreated())
        .andReturn();

    // HTTP 응답을 그냥 문자열로 가져옴 (JSON형태긴함)
    String userResponse = userResult.getResponse().getContentAsString();

    // objectMapper로 JSON으로 만들기
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
  @DisplayName("User API 통합 테스트")
  class UserApiTest {

    @Test
    @DisplayName("유저 정보 수정 및 조회")
    void update_and_get_user() throws Exception {
      // 수정 요청 DTO
      UserUpdateRequest updateRequest = new UserUpdateRequest("Rex - 수정", null, null);

      MockMultipartFile updatePart = new MockMultipartFile(
          "userUpdateRequest",
          "",
          MediaType.APPLICATION_JSON_VALUE,
          objectMapper.writeValueAsBytes(updateRequest)
      );

      mockMvc.perform(multipart("/api/users/{userId}", testUserId)
              .file(updatePart)
              .with(request -> {
                // multipart의 기본 옵션이 POST라 PATCH로 바꿔야함
                request.setMethod("PATCH");
                return request;
              }))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.username").value("Rex - 수정"));
    }

    @Test
    @DisplayName("유저 삭제")
    void delete_user() throws Exception {
      mockMvc.perform(delete("/api/users/{userId}", testUserId))
          .andExpect(status().isNoContent());

      // 삭제 확인 (DB 조회 시 없어야 함)
      assertThat(userRepository.findById(testUserId)).isEmpty();
    }
  }

  @Nested
  @DisplayName("Channel API 통합 테스트")
  class ChannelApiTest {

    @Test
    @DisplayName("채널 정보 수정")
    void update_channel() throws Exception {
      PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest("Rex 채팅방 - 수정",
          "Rex 채팅방 입니다. - 수정");

      mockMvc.perform(patch("/api/channels/{channelId}", testChannelId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(updateRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value("Rex 채팅방 - 수정"))
          .andExpect(jsonPath("$.description").value("Rex 채팅방 입니다. - 수정"));
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
    @DisplayName("메세지 수정 및 목록 조회")
    void update_and_list_messages() throws Exception {
      // 수정
      MessageUpdateRequest updateRequest = new MessageUpdateRequest("메세지 입니다. - 수정");
      mockMvc.perform(patch("/api/messages/{messageId}", testMessageId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(updateRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").value("메세지 입니다. - 수정"));

      // 목록 조회
      mockMvc.perform(get("/api/messages")
              .param("channelId", testChannelId.toString()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content[0].content").value("메세지 입니다. - 수정"));
    }

    @Test
    @DisplayName("메세지 삭제")
    void delete_message() throws Exception {
      mockMvc.perform(delete("/api/messages/{messageId}", testMessageId))
          .andExpect(status().isNoContent());

      assertThat(messageRepository.findById(testMessageId)).isEmpty();
    }
  }
}
