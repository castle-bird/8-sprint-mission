package com.sprint.mission.discodeit.user;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class) // UserController만 로드하여 테스트
@DisplayName("UserController 단위 테스트")
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc; // MockMvc를 활용해 컨트롤러 테스트

  @Autowired
  private ObjectMapper objectMapper; // 객체를 JSON 문자열로 변환하기 위함

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserStatusService userStatusService;


  @Test
  @DisplayName("성공: 프로필 이미지 없이 유저 생성 요청된다.")
  void create_user_without_profile() throws Exception {
    // Given
    UserCreateRequest request = new UserCreateRequest("Rex", "rex@gmail.com", "password123");
    UserDto responseDto = new UserDto(UUID.randomUUID(), "Rex", "rex@gmail.com", null, true);

    // 컨트롤러에서 MultipartFile로 받아서 변경
    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest",                    // 파트 이름 (컨트롤러 @RequestPart와 일치해야 함)
        "",                                     // 오리지널 파일 이름 (데이터 파트라 보통 비워둠)
        MediaType.APPLICATION_JSON_VALUE,       // 이 파트의 데이터 형식 (JSON임을 명시)
        
        // 실제 데이터 (객체를 바이트로 변환) - 클라쪽에서 Multipart로 데이터를 보내기때문에 서버 입장에서 byte로 받음
        objectMapper.writeValueAsBytes(request)
    );

    MockMultipartFile filePart = new MockMultipartFile(
        "profile",
        "",
        MediaType.IMAGE_PNG_VALUE,
        new byte[0]
    );

    given(
        userService.create(any(UserCreateRequest.class), any(Optional.class))
    ).willReturn(responseDto);

    // When & Then
    mockMvc.perform(multipart("/api/users")
            .file(requestPart)
            .file(filePart))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("Rex"))
        .andExpect(jsonPath("$.email").value("rex@gmail.com"))
        .andExpect(jsonPath("$.online").value(true))
        .andExpect(jsonPath("$.profile").value(nullValue()));
  }

  @Test
  @DisplayName("성공: 프로필 이미지가 있어도 유저 생성 요청된다.")
  void create_user_with_profile() throws Exception {
    // Given
    UserCreateRequest request = new UserCreateRequest("Rex", "rex@gmail.com", "password123");
    UserDto responseDto = new UserDto(UUID.randomUUID(), "Rex", "rex@gmail.com", null, true);

    // 컨트롤러에서 MultipartFile로 받아서 변경
    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    MockMultipartFile filePart = new MockMultipartFile(
        "profile",
        "",
        MediaType.IMAGE_PNG_VALUE,
        new byte[100]
    );

    given(
        userService.create(any(UserCreateRequest.class), any(Optional.class))
    ).willReturn(responseDto);

    // When & Then
    mockMvc.perform(multipart("/api/users")
            .file(requestPart)
            .file(filePart))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("Rex"))
        .andExpect(jsonPath("$.email").value("rex@gmail.com"))
        .andExpect(jsonPath("$.online").value(true));
  }
}