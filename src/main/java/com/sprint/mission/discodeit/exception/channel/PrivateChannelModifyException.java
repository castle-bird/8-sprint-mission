package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.global.ErrorCode;
import java.util.Map;

public class PrivateChannelModifyException extends ChannelException {

  public PrivateChannelModifyException() {
    super(ErrorCode.PRIVATE_CHANNEL_UPDATE);
  }

  public PrivateChannelModifyException(Map<String, Object> details) {
    super(ErrorCode.PRIVATE_CHANNEL_UPDATE, details);
  }
}
