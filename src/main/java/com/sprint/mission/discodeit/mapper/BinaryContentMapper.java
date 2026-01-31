package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.io.InputStream;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BinaryContentMapper {

  private BinaryContentStorage binaryContentStorage;

  @Autowired
  public void set(BinaryContentStorage binaryContentStorage) {
    this.binaryContentStorage = binaryContentStorage;
  }

  @Mapping(target = "bytes", expression = "java(getBytes(binaryContent))")
  public abstract BinaryContentDto toBinaryContentDto(BinaryContent binaryContent);


  @Named("getBytes")
  protected byte[] getBytes(BinaryContent binaryContent) {
    if (binaryContent == null || binaryContent.getId() == null) {
      return null;
    }

    try (InputStream inputStream = binaryContentStorage.get(binaryContent.getId())) {

      return inputStream.readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
