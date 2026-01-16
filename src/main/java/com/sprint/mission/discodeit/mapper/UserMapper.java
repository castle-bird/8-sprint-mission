package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

// uses={}: 다른 Mapping클래스 상속 느낌  
@Mapper(
    componentModel = "spring",
    uses = {BinaryContentMapper.class}
)
public abstract class UserMapper {

  private UserStatusRepository userStatusRepository;

  @Autowired
  public void set(UserStatusRepository repo) {
    this.userStatusRepository = repo;
  }

  /**
   * 매핑 하기
   *
   * 매핑필드 자동변환말고 수동으로 지정 가능
   * target= 매핑될 필드이름
   * expression="java( 메서드명 )" / @Named로 매칭
   * qualifiedByName: 위에 uses에서 작성한 클래스에서 사용할 메서드 / @Named로 매칭
   */
  @Mapping(target = "online", expression = "java(getOnline(user))")
  @Mapping(target = "profile", qualifiedByName = "getBinaryContentDto")
  public abstract UserDto toUserDto(User user);

  // 프로필 DTO로 변환
  @Named("getBinaryContentDto")
  protected abstract BinaryContentDto getBinaryContentDto(BinaryContent profile);

  // 온라인 체크
  @Named("getOnline")
  protected Boolean getOnline(User user) {
    return userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline).orElse(null);
  }
}