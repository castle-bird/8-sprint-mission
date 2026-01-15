package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "channels")
@Getter
// Lombok의 @Builder사용으로 인스턴스 만들것이기에 무분별한 객체생성을 막기 위함
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @Enumerated(EnumType.STRING) // Java Enum 이름을 문자열로 변환
  @JdbcTypeCode(SqlTypes.NAMED_ENUM) // PostgreSQL의 사용자 정의 ENUM 타입('channel_type')임을 명시
  @Column(name = "type", nullable = false, columnDefinition = "channel_type")
  private ChannelType type;

  @OneToMany(mappedBy = "channel", cascade = CascadeType.REMOVE)
  private List<Message> messages;

  @OneToMany(mappedBy = "channel", cascade = CascadeType.REMOVE)
  private List<ReadStatus> readStatuses;

  @Builder
  public Channel(String name, String description, ChannelType type) {
    this.name = name;
    this.description = description;
    this.type = type;
  }

  public void update(String newName, String newDescription) {
    boolean anyValueUpdated = false;
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
      anyValueUpdated = true;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
      anyValueUpdated = true;
    }
  }
}
