package com.sprint.mission.discodeit.entity.base;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass // 상속받는 자식 엔티티에게 매핑 정보만 제공 - BaseEntity는 테이블 안만들게
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseUpdatableEntity extends BaseEntity {
  // 수정 불가한 Entity는 이것을 상속하지 않는다.

  @LastModifiedDate
  @Column(name = "updated_at")
  private Instant updatedAt;
}
