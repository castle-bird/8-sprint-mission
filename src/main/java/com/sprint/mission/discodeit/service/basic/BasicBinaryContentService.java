package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  public BinaryContentDto create(BinaryContentCreateRequest request) {
    // 생성

    String fileName = request.fileName();
    String contentType = request.contentType();
    byte[] bytes = request.bytes();

    BinaryContent binaryContent = BinaryContent.builder()
        .fileName(fileName)
        .contentType(contentType)
        .bytes(bytes)
        .build();

    return binaryContentMapper.toBinaryContentDto(binaryContentRepository.save(binaryContent));
  }

  @Override
  public BinaryContentDto find(UUID binaryContentId) {
    // 단건 찾기
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new NoSuchElementException(
            "BinaryContent with id " + binaryContentId + " not found"));

    return binaryContentMapper.toBinaryContentDto(binaryContent);
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    // 다건 찾기

    return binaryContentRepository.findAllById(binaryContentIds).stream()
        .map(binaryContentMapper::toBinaryContentDto)
        .toList();
  }

  @Override
  public void delete(UUID binaryContentId) {
    // 삭제

    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
    }
    binaryContentRepository.deleteById(binaryContentId);
  }

}
