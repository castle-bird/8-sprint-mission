package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class DiscodeitUserDetails implements UserDetails {

  private final User user;

  public User getUser() {
    return this.user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
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
    // 자기 자신과 비교
    if (this == o) {
      return true;
    }
    // 타입 비교
    if (!(o instanceof DiscodeitUserDetails that)) {
      return false;
    }
    // 사용자 이름 비교
    return Objects.equals(user.getUsername(), that.user.getUsername());
  }

  @Override
  public int hashCode() {
    // 사용자 이름 해시 코드 반환
    return Objects.hashCode(user.getUsername());
  }
}
