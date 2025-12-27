package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final ResourceLoader resourceLoader;

    // 실제 파일이 저장될 루트 경로 (예: 프로젝트 루트의 uploads 폴더)
    private final Path rootLocation = Paths.get("uploads/images");

    @Override
    public BinaryContent create(BinaryContentCreateRequest request) {
        String fileName = request.fileName();
        byte[] bytes = request.bytes();
        String contentType = request.contentType();
        BinaryContent binaryContent = new BinaryContent(
                fileName,
                (long) bytes.length,
                contentType,
                bytes
        );
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found"));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
                .toList();
    }

    @Override
    public void delete(UUID binaryContentId) {
        if (!binaryContentRepository.existsById(binaryContentId)) {
            throw new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
        }
        binaryContentRepository.deleteById(binaryContentId);
    }

    @Override
    public void saveMultiFiles(List<MultipartFile> files) {

    }

    @Override
    public void saveSingleFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return;

        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

            String originalName = Objects.requireNonNull(file.getOriginalFilename());
            String savedName = UUID.randomUUID() + "_" + originalName;
            Path targetPath = rootLocation.resolve(savedName);

            // StandardCopyOption.REPLACE_EXISTING: 중복 파일 있으면 에러 안내고 덮기
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(originalName, file.getContentType(), Files.readAllBytes(targetPath));
            this.create(binaryContentCreateRequest);
        } catch (IOException e) {
            throw new RuntimeException("[BinaryContentService] 파일 저장 중 오류 발생", e);
        }
    }
}
