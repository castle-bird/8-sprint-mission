package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.global.ErrorCode;
import java.util.Map;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException() {
    super(ErrorCode.READ_STATUS_ERROR);
  }

  public ReadStatusNotFoundException(Map<String, Object> details) {
    super(ErrorCode.READ_STATUS_NOT_FOUND, details);
  }
}
