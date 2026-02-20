package com.sprint.mission.discodeit.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.impl.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
@DisplayName("UserService 단위 테스트")
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserStatusRepository userStatusRepository;
  @Mock
  private UserMapper userMapper;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private BinaryContentStorage binaryContentStorage;

  @InjectMocks
  private BasicUserService userService;


  @Nested
  @DisplayName("유저 생성 테스트")
  class user_create {

    private UserCreateRequest userCreateRequest;

    @BeforeEach
    void setUp() {
      userCreateRequest = new UserCreateRequest("Rex", "rex@gmail.com", "12345678");

      // 중복 Given
      // 생성할때 이메일, 이름 중복검사 시행 했는지 체크
      given(userRepository.existsByEmail(anyString())).willReturn(false);
      given(userRepository.existsByUsername(anyString())).willReturn(false);
    }

    @Test
    @DisplayName("성공: 프로필이 없어도 유저가 생성된다.")
    void create_user_without_profile() {
      // Given
      UserDto expectedDto = new UserDto(
          UUID.randomUUID(),
          "Rex",
          "rex@gmail.com",
          null,
          true
      );
      given(
          userMapper.toDto(any())
      ).willReturn(expectedDto);

      // When
      UserDto result = userService.create(userCreateRequest, Optional.empty());

      // Then
      then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("성공: 프로필 이미지를 포함하여 유저가 생성된다.")
    void create_user_with_profile() {
      // Given
      BinaryContentCreateRequest profileRequest = new BinaryContentCreateRequest(
          "rex.png",
          "image/png",
          "rex-image".getBytes()
      );

      BinaryContentDto binaryContentDto = new BinaryContentDto(
          UUID.randomUUID(),
          "rex.png",
          (long) "rex-image".getBytes().length,
          "image/png"
      );

      UserDto expectedDto = new UserDto(
          UUID.randomUUID(),
          "Rex",
          "rex@gmail.com",
          binaryContentDto,
          true
      );

      given(
          userMapper.toDto(any())
      ).willReturn(expectedDto);

      // When
      UserDto result = userService.create(userCreateRequest, Optional.of(profileRequest));

      // Then
      then(userRepository).should().save(any(User.class));
      then(binaryContentRepository).should().save(any(BinaryContent.class));
      then(binaryContentStorage).should().put(any(), any());
    }
  }

  // ================================================================================================

  @Nested
  @DisplayName("유저 수정 테스트")
  class user_update {

    private User findUser;
    private UserUpdateRequest userUpdateRequest;

    @BeforeEach
    void setUp() {
      findUser = new User("Rex", "rex@gmail.com", "12345678", null);
      userUpdateRequest = new UserUpdateRequest("Rex-수정", "rex@gmail.com-수정", "12345678-수정");

      // 중복 Given
      // 생성할때 아이디, 이메일, 이름 중복검사 시행 했는지 체크
      given(
          userRepository.findById(any())
      ).willReturn(
          Optional.of(findUser)
      );
      given(userRepository.existsByEmail(anyString())).willReturn(false);
      given(userRepository.existsByUsername(anyString())).willReturn(false);
    }

    @Test
    @DisplayName("성공: 프로필 없이도 유저 수정이 된다.")
    void update_user_without_profile() {
      // Given
      UserDto expectedDto = new UserDto(
          UUID.randomUUID(),
          "Rex-수정",
          "rex@gmail.com-수정",
          null,
          true
      );

      given(
          userMapper.toDto(any())
      ).willReturn(expectedDto);

      // When
      UserDto result = userService.update(
          UUID.randomUUID(),
          userUpdateRequest,
          Optional.empty()
      );

      // Then
      // 기존 유저와 수정 유저가 이름이 같은지?
      assertEquals(expectedDto.username(), findUser.getUsername());
    }

    @Test
    @DisplayName("성공: 프로필 있어도 유저 수정이 된다.")
    void update_user_with_profile() {
      // Given
      BinaryContentCreateRequest profileRequest = new BinaryContentCreateRequest(
          "rex.png",
          "image/png",
          "rex-image".getBytes()
      );

      BinaryContentDto binaryContentDto = new BinaryContentDto(
          UUID.randomUUID(),
          "rex.png",
          (long) "rex-image".getBytes().length,
          "image/png"
      );

      UserDto expectedDto = new UserDto(
          UUID.randomUUID(),
          "Rex-수정",
          "rex@gmail.com-수정",
          binaryContentDto,
          true
      );

      // When
      UserDto result = userService.update(
          UUID.randomUUID(),
          userUpdateRequest,
          Optional.of(profileRequest)
      );

      // Then
      then(binaryContentRepository).should().save(any(BinaryContent.class));
      then(binaryContentStorage).should().put(any(), any());
      assertEquals(expectedDto.username(), findUser.getUsername());
    }
  }
}