package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository; // 추가

  @Override
  public UserDto login(LoginRequest loginRequest) { // 반환 타입 변경
    String username = loginRequest.username();
    String password = loginRequest.password();

    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new NoSuchElementException("User with username " + username + " not found"));

    if (!user.getPassword().equals(password)) {
      throw new IllegalArgumentException("Wrong password");
    }

    return toDto(user); // DTO로 변환하여 반환
  }

  // BasicUserService의 toDto와 동일한 로직 (중복 제거를 위해 공통화하는 것이 좋음)
  private UserDto toDto(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);

    BinaryContentDto profileDto = Optional.ofNullable(user.getProfile())
        .map(profile -> BinaryContentDto.builder()
            .id(profile.getId())
            .fileName(profile.getFileName())
            .contentType(profile.getContentType())
            .size(profile.getSize())
            .bytes(profile.getBytes())
            .build())
        .orElse(null);

    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .profile(profileDto)
        .online(online)
        .build();
  }
}
