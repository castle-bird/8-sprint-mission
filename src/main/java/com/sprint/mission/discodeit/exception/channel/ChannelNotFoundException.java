package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.global.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserException;
import java.util.Map;

public class ChannelNotFoundException extends UserException {

  public ChannelNotFoundException() {
    super(ErrorCode.CHANNEL_NOT_FOUND);
  }

  public ChannelNotFoundException(Map<String, Object> details) {
    super(ErrorCode.CHANNEL_NOT_FOUND, details);
  }
}
