package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.global.DiscodeitException;
import com.sprint.mission.discodeit.exception.global.ErrorCode;
import java.util.Map;

public class UserException extends DiscodeitException {

  // 글로벌 유저 에러
  public UserException() {
    super(ErrorCode.USER_ERROR);
  }

  public UserException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
