package com.sprint.mission.discodeit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  @Mapping(target = "userId", source = "user.id")
  UserStatusDto toUserStatusDto(UserStatus userStatus);
}
