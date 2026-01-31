package com.sprint.mission.discodeit.storage.impl;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

@Service
@Slf4j
public class BinaryContentStorageImpl implements BinaryContentStorage {

  private final Path DIRECTORY; // → .discodeit

  public BinaryContentStorageImpl(
      @Value("${discodeit.repository.file-directory}") String fileDirectory
  ) {
    this.DIRECTORY = Path.of(System.getProperty("user.dir"), fileDirectory); // 프로젝트 최상위 기준

    // 폴더 만들기
    if (Files.notExists(DIRECTORY)) {
      try {
        Files.createDirectory(DIRECTORY);
      } catch (Exception e) {
        log.error("폴더 생성 실패: {}", DIRECTORY);
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    // ID로 저장
    Path filePath = DIRECTORY.resolve(id.toString());

    try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
      fos.write(bytes);
      log.info("파일 저장 완료: {}", filePath);

      return id;
    } catch (Exception e) {

      throw new RuntimeException("파일 저장 실패", e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    Path filePath = DIRECTORY.resolve(id.toString());

    try {
      if (Files.notExists(filePath)) {
        // 파일이 없는 경우 로그를 남기고 명확한 예외를 던짐
        log.error("파일이 존재하지 않습니다: {}", filePath.toAbsolutePath());
        throw new NoSuchElementException("파일을 찾을 수 없습니다: " + id);
      }

      return Files.newInputStream(filePath);
    } catch (NoSuchElementException e) {
      // 이미 처리된 예외는 그대로 던짐
      throw e;
    } catch (Exception e) {
      // 그 외의 입출력 에러 등
      log.error("파일 읽기 중 오류 발생: {}, 사유: {}", filePath, e.getMessage());
      throw new RuntimeException("파일 읽기 실패", e);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    try {
      // 1. 파일 읽기
      InputStream inputStream = this.get(binaryContentDto.id());
      InputStreamResource resource = new InputStreamResource(inputStream);

      // 2. 파일명 인코딩
      String encodedFileName = UriUtils.encode(binaryContentDto.fileName(), StandardCharsets.UTF_8);

      // 3. Content-Disposition 헤더 설정
      String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

      // 4. ResponseEntity 생성 및 반환
      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(binaryContentDto.contentType())) // 예: image/png
          .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
          .body(resource);

    } catch (Exception e) {
      throw new RuntimeException("파일 다운로드 준비 실패", e);
    }
  }
}
