package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Security에서 사용하는 사용자 정보 래퍼 클래스
 * <p>
 * {@link User} 엔티티를 {@link UserDetails}로 어댑트하여 Spring Security에서
 * 인증 및 권한 검증에 사용할 수 있도록 한다.
 * <p>
 * 현재 권한 시스템:
 * - 구현 단계: 모든 사용자가 동일한 권한 레벨 (ROLE_USER 미설정)
 * - 확장 계획: 향후 역할 기반 권한(Role-based Authorization)이 필요한 경우,
 *   User 엔티티에 role 필드를 추가하고 getAuthorities()를 구현하면 됨
 *   예) List.of(new SimpleGrantedAuthority("ROLE_USER"))
 */
@RequiredArgsConstructor
public class DiscodeitUserDetails implements UserDetails {

  private final User user;

  public User getUser() {
    return this.user;
  }

  /**
   * 사용자의 권한 목록 반환
   * <p>
   * 현재는 권한 검증 없이 모든 인증된 사용자가 동일하므로, 빈 리스트를 반환한다.
   * 역할 기반 권한이 필요해지면 User에 role 필드를 추가하고 여기서 구현한다.
   *
   * @return 권한 목록 (현재는 빈 리스트)
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // TODO: 향후 권한 시스템 도입 시 구현
    // return List.of(new SimpleGrantedAuthority(user.getRole().getAuthority()));
    return Collections.emptyList();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DiscodeitUserDetails that)) {
      return false;
    }
    return Objects.equals(user.getUsername(), that.user.getUsername());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(user.getUsername());
  }
}
