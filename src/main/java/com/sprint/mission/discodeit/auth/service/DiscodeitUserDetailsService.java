package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security 사용자 정보 로드 서비스
 * <p>
 * 사용자명(username)으로 데이터베이스에서 사용자를 조회하고,
 * {@link DiscodeitUserDetails}로 감싸서 Spring Security에 제공한다.
 */
@Service
@RequiredArgsConstructor
public class DiscodeitUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * 사용자명으로 사용자 정보 조회
   * <p>
   * 로그인 시 입력된 사용자명으로 데이터베이스에서 사용자를 조회한다.
   * 사용자가 존재하지 않으면 {@link UsernameNotFoundException}을 던진다.
   *
   * @param username 조회할 사용자명
   * @return 사용자 정보가 포함된 {@link UserDetails}
   * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(DiscodeitUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}


