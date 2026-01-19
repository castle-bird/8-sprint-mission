package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findAllByChannelId(UUID channelId);

  //  Page<Message> findAllByChannelId(UUID channelId, Pageable pageable);
  // Page 사용시 불필요한 Count쿼리 나감.
  // 미션에는 총 메세지 몇 개인지 알 필요 없다고 나옴.
  Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);
}
