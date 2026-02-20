package com.sprint.mission.discodeit.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@DisplayName("UserRepository 단위 테스트")
//@Transactional: @DataJpaTest에 이미 있어서 안써도 됨 
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;


  @Test
  @DisplayName("성공: 실제 DB에 User가 저장된다.")
  void create_user() {
    // Given
    User user = new User("Rex", "rex@gmail.com", "12345678", null);

    // When
    User savedUser = userRepository.save(user);

    // Then
    assertEquals(savedUser.getUsername(), user.getUsername());
    assertEquals(savedUser.getEmail(), user.getEmail());
    assertNotNull(savedUser.getCreatedAt());
  }

  @Test
  @DisplayName("성공: 실제 DB에 있는 User가 수정된다.")
  void update_user() {
    // Given
    User user = new User("Rex", "rex@gmail.com", "12345678", null);
    userRepository.save(user);

    // When
    User findUser = userRepository.findById(user.getId())
        .orElseThrow(() -> new UserNotFoundException(Map.of("requestedId", user.getId())));

    findUser.update(
        "Rex-수정",
        "rex@gmail.com-수정",
        null,
        null
    );

    // Auditing 트리거
    // 위에는 1차캐시에서 수정한거라 DB반영이 아직 안됨
    userRepository.saveAndFlush(findUser);

    // Then
    assertEquals("Rex-수정", user.getUsername());
    assertEquals("Rex-수정", findUser.getUsername());
    //초기 생성시 생성/수정 날짜 똑같이 생성 했는데, 수정 했으니 바뀌어야함
    assertNotEquals(findUser.getCreatedAt(), findUser.getUpdatedAt());
  }
}
