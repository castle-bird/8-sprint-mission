package com.sprint.mission.discodeit.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 단위 테스트")
public class UserServiceTest {

  // 테스트 대상이 사용하는 의존성들 Mock으로 넣어줘야함 - 필수
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

  // 테스트 대상 - 위에 Mock들을 넣어줌
  @InjectMocks
  private BasicUserService userService;

  @Test
  @DisplayName("성공: 프로필이 없어도 유저가 생성된다.")
  void create_user_without_profile() {
    /**
     * Given
     */
    UserCreateRequest request = new UserCreateRequest("Rex", "rex@gmail.com", "12345678");
    UserDto expectedDto = new UserDto(UUID.randomUUID(), "Rex", "rex@gmail.com", null, true);

    /* Service단 테스트에서 Repository의 영향을 배제하기 위해 아래처럼 가정 */
    // 이메일 중복 없음을 가정
    given(
        userRepository.existsByEmail(anyString())
    ).willReturn(false);

    // 이름 중복 없음을 가정
    given(
        userRepository.existsByUsername(anyString())
    ).willReturn(false);

    // 매퍼가 DTO 반환하도록 가정
    given(
        userMapper.toDto(any())
    ).willReturn(expectedDto);

    /**
     * When
     */
    UserDto result = userService.create(request, Optional.empty());

    /**
     * Then
     */
    assertThat(result).isNotNull();
    assertEquals(result.username(), request.username());
    assertEquals(result.email(), request.email());

    // 실제로 Repository가 호출 됐는지?
    then(userRepository).should(times(1)).existsByEmail(request.email());
    then(userRepository).should(times(1)).existsByUsername(request.username());
    then(userRepository).should(times(1)).save(any(User.class));
  }

  @Test
  @DisplayName("성공: 프로필 이미지를 포함하여 유저가 생성된다.")
  void create_user_with_profile() {
    /**
     * Given
     */
    UserCreateRequest request = new UserCreateRequest("Rex", "rex@gmail.com", "12345678");
    BinaryContentCreateRequest profileRequest = new BinaryContentCreateRequest(
        "rex.png", "image/png", "rex-image".getBytes()
    );
    BinaryContentDto binaryContentDto = new BinaryContentDto(UUID.randomUUID(), "rex.png",
        (long) "rex-image".getBytes().length, "image/png");
    UserDto expectedDto = new UserDto(UUID.randomUUID(), "Rex", "rex@gmail.com", binaryContentDto,
        true);

    // 이메일 중복 없음을 가정
    given(
        userRepository.existsByEmail(anyString())
    ).willReturn(false);

    // 이름 중복 없음을 가정
    given(
        userRepository.existsByUsername(anyString())
    ).willReturn(false);

    // 매퍼가 DTO 반환하도록 가정
    given(
        userMapper.toDto(any())
    ).willReturn(expectedDto);

    /**
     * When
     */
    UserDto result = userService.create(request, Optional.of(profileRequest));

    assertThat(result).isNotNull();
    assertEquals(result.username(), request.username());
    assertEquals(result.email(), request.email());

    /**
     * Then
     */
    // 실제로 Repository가 호출 됐는지?
    then(userRepository).should(times(1)).existsByEmail(request.email());
    then(userRepository).should(times(1)).existsByUsername(request.username());
    then(userRepository).should(times(1)).save(any(User.class));
    then(binaryContentRepository).should(times(1)).save(any(BinaryContent.class));
    then(binaryContentStorage).should(times(1)).put(any(), any());
  }
}