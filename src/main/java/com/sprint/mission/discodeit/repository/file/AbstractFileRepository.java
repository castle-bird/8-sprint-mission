package com.sprint.mission.discodeit.repository.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractFileRepository<T> {
    protected final Path path;
    protected Map<UUID, T> data;

    protected AbstractFileRepository(Path path) {
        this.path = path;
        loadFile();
    }

    private void loadFile() {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent()); // 디렉토리 없으면 생성
                data = new HashMap<>();

                return;
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: " + path.getParent(), e);
            }
        }

        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            Object obj = ois.readObject();
            data = (Map<UUID, T>) obj;
        } catch (Exception e) {
            throw new RuntimeException("파일 로드 실패: " + path, e);
        }
    }

    protected void saveFile() {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(data);
        } catch (Exception e) {
            throw new RuntimeException("파일 저장 실패: " + path, e);
        }
    }
}