package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.global.ErrorCode;
import java.util.Map;

public class UserDuplicateException extends UserException {

  public UserDuplicateException() {
    super(ErrorCode.USER_DUPLICATE);
  }

  public UserDuplicateException(Map<String, Object> details) {
    super(ErrorCode.USER_DUPLICATE, details);
  }
}
