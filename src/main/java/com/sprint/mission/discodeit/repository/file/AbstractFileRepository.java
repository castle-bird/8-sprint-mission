package com.sprint.mission.discodeit.repository.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class AbstractFileRepository<T> {
    protected final Path dirPath;                   // 파일이 저장될 directory 경로
    protected Map<UUID, T> data = new HashMap<>();  // 역직렬화된 파일을 저장할 필드 - 메모리 용

    protected AbstractFileRepository(Path path) {
        this.dirPath = path;
        loadFiles(); // 첫 로딩시 파일 목록들을 읽기 위함
    }

    /**
     * ***************************
     * 1. dirPath의 경로로 파일을 읽어 역직렬화 한다.
     * ***************************
     */
    private void loadFiles() {
        try {
            // 1). 일단 폴더 체크
            if (!Files.exists(dirPath)) {
                // 2). 폴더가 없으면 만들고 return
                Files.createDirectories(dirPath);

                return;
            }

            // 3). 폴더가 있으면 파일 체크
            try (Stream<Path> files = Files.list(dirPath)) {
                // Files.list(PATH): PATH에 있는 파일들을 Stream<Path>로 return 해준다.

                // 각 파일들을 역직렬화 하여 data에 넣기 위함. - .ser로 끝나는 파일만 체크 
                files
                        .filter(file -> file.toString().endsWith(".ser"))
                        .forEach(this::fileToObject);
            }

        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패" + e);
        }
    }

    // 각 file을의 역직렬화를 위함
    private void fileToObject(Path file) {
        try (
                FileInputStream fis = new FileInputStream(file.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            // Path.toFile(): Path가 나타내는 경로(파일명 포함)를 File 객체로 변환한다.
            // FileInputStream(File): 해당 File로부터 바이트 단위 데이터를 읽기 위한 입력 스트림이다.
            // ObjectInputStream(InputStream): 스트림으로부터 데이터를 읽어 Java 객체로 복원한다.
            // 정리: FileInputStream로 외부 파일과 java어플리케이션 간의 연결 통로를 만들고, ObjectInputStream로 java객체로 만듦

            T result = (T) ois.readObject();
            UUID id = getId(result);

            data.put(id, result);
        } catch (Exception e) {
            throw new RuntimeException("파일 역직렬화 실패: " + e);
        }
    }

    // fileToObject에서 파일을 객체로 변환까지는 이상 없었는데
    // Id값을 따로 추출 해야함.
    // T result = result.getId -> 이게 안됨. 데이터 타입이 뭔지 모르니 getId가 안됨..
    protected abstract UUID getId(T result);

    /**
     * ***************************
     * 2. 직렬화 및 파일로 저장
     * ***************************
     */
    protected void objectToFile(T object) {
        UUID id = getId(object);

        // Path.resolve(): Path에 경로 이어붙이기
        Path filePath = dirPath.resolve(id.toString() + ".ser");

        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(object);
        } catch (Exception e) {
            throw new RuntimeException("파일 저장 실패: " + e);
        }
    }

    /**
     * ***************************
     * 3. 파일 삭제
     * ***************************
     */
    protected void deleteFile(UUID id) {
        Path filePath = dirPath.resolve(id.toString() + ".ser");

        try {
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제 실패: " + e);
        }
    }
}