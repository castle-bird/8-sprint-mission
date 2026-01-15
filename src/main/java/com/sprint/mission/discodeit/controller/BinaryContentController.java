package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;

  @Override
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> find(@PathVariable UUID binaryContentId) {
    BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);

    return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
  }

  @Override
  @GetMapping()
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds
  ) {
    List<BinaryContentDto> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);

    return ResponseEntity.status(HttpStatus.OK).body(binaryContents);
  }
}
