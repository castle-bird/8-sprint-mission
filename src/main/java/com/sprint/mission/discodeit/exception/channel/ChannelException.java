package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.global.DiscodeitException;
import com.sprint.mission.discodeit.exception.global.ErrorCode;
import java.util.Map;

public class ChannelException extends DiscodeitException {

  // 글로벌 채널 에러
  public ChannelException() {
    super(ErrorCode.CHANNEL_ERROR);
  }

  public ChannelException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ChannelException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
