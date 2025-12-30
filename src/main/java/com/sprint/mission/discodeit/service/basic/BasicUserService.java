package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    //
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentService binaryContentService;

    @Override
    public UserDto create(UserCreateRequest userCreateRequest, BinaryContentCreateRequest profileCreateRequest) {
        String username = userCreateRequest.username();
        String email = userCreateRequest.email();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("[BasicUserService] create(): " + email + "은 이미 존재하는 이메일입니다.");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("[BasicUserService] create(): " + username + "은 이미 존재하는 사용자 이름입니다.");
        }

        // Optional체크를 매개변수에서 했는데
        // Optional은 설계의 의도가 반환값을 null체크하기 위해 만들어진 것이므로 변경함
        UUID nullableProfileId = Optional.ofNullable(profileCreateRequest)
                .map(req -> {
                    String fileName = req.fileName();
                    String contentType = req.contentType();
                    byte[] bytes = req.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);

                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        String password = userCreateRequest.password();

        User user = new User(username, email, password, nullableProfileId);
        User createdUser = userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(createdUser.getId(), now);
        userStatusRepository.save(userStatus);

        return toDto(createdUser);
    }

    @Override
    public UserDto find(UUID userId) {
        return userRepository.findById(userId)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("[BasicUserService] find(): " + userId + "를 찾을 수 없습니다."));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserDto update(
            UUID userId,
            UserUpdateRequest userUpdateRequest,
            BinaryContentCreateRequest profileCreateRequest,
            MultipartFile profileFile
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("[BasicUserService] update(): " + userId + "를 찾을 수 없습니다."));

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();
        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("[BasicUserService] update(): " + newEmail + "은 이미 존재하는 이메일입니다.");
        }
        if (userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("[BasicUserService] update(): " + newUsername + "은 이미 존재하는 이름입니다.");
        }

        // 프로필 파일이 있다면 추가
        Optional.ofNullable(profileFile)
                .ifPresent(binaryContentService::saveSingleFile);

        UUID nullableProfileId = Optional.ofNullable(profileCreateRequest)
                .map(profileRequest -> {

                    Optional.ofNullable(user.getProfileId())
                            .ifPresent(binaryContentRepository::deleteById);

                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        String newPassword = userUpdateRequest.newPassword();
        user.update(newUsername, newEmail, newPassword, nullableProfileId);

        return toDto(userRepository.save(user));
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("[BasicUserService] delete(): " + userId + "를 찾을 수 없습니다."));

        Optional.ofNullable(user.getProfileId())
                .ifPresent(binaryContentRepository::deleteById);
        userStatusRepository.deleteByUserId(userId);

        userRepository.deleteById(userId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private UserDto toDto(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(null);

        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                online
        );
    }
}
