package com.sprint.mission.discodeit.service.impl;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public UserDto create(
      UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest optionalProfileCreateRequest
  ) {
    // 유저 생성
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    // 이메일 중복체크
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }

    // 이름 중복체크
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("User with username " + username + " already exists");
    }

    // 프로필
    BinaryContent nullableProfile = Optional.ofNullable(optionalProfileCreateRequest)
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();

          BinaryContent binaryContent = BinaryContent.builder()
              .fileName(fileName)
              .size((long) bytes.length)
              .contentType(contentType)
              .build();

          // 프로필 이미지 정보만 저장
          BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

          // 프로필 이미지 로컬 저장
          binaryContentStorage.put(savedBinaryContent.getId(), bytes);

          return savedBinaryContent;
        }).orElse(null);

    // 비밀번호
    String password = userCreateRequest.password();

    // 위 정보로 유저 생성
    User user = User.builder()
        .username(username)
        .email(email)
        .password(password)
        .profile(nullableProfile)
        .build();

    User createdUser = userRepository.save(user);

    // 현재 시간을 접속 시간으로 설정
    Instant now = Instant.now();

    UserStatus userStatus = UserStatus.builder()
        .user(createdUser)
        .lastActiveAt(now)
        .build();

    userStatusRepository.save(userStatus);

    return userMapper.toUserDto(createdUser);
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toUserDto)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(userMapper::toUserDto)
        .toList();
  }

  @Override
  @Transactional
  public UserDto update(
      UUID userId,
      UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest optionalProfileCreateRequest
  ) {

    // 유저 체크
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();

    // 이메일 중복 체크
    if (userRepository.existsByEmail(newEmail)) {
      throw new IllegalArgumentException("User with email " + newEmail + " already exists");
    }

    // 이름 중복 체크
    if (userRepository.existsByUsername(newUsername)) {
      throw new IllegalArgumentException("User with username " + newUsername + " already exists");
    }

    // 프로필
    BinaryContent nullableNewProfile = Optional.ofNullable(optionalProfileCreateRequest)
        .map(profileRequest -> {

          // 프로필 있으면 삭제
          Optional.ofNullable(user.getProfile())
              .ifPresent(profile -> binaryContentRepository.deleteById(profile.getId()));

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();

          BinaryContent newProfile = BinaryContent.builder()
              .fileName(fileName)
              .size((long) bytes.length)
              .contentType(contentType)
              .build();

          // 프로필 이미지 정보만 저장
          BinaryContent savedNewProfile = binaryContentRepository.save(newProfile);

          // 프로필 이미지 로컬 저장
          binaryContentStorage.put(savedNewProfile.getId(), bytes);

          return savedNewProfile;
        })
        .orElse(null);

    // 비밀번호
    String newPassword = userUpdateRequest.newPassword();

    // 위에서 찾은 유저 업데이트
    user.update(
        newUsername,
        newEmail,
        newPassword,
        nullableNewProfile
    );

    return userMapper.toUserDto(userRepository.save(user));
  }

  @Override
  public void delete(UUID userId) {
    // 삭제

    // 유저 체크
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    userRepository.delete(user);
  }
}
