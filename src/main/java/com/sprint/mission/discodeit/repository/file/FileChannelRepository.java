package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static FileChannelRepository instance;
    private final Path path = Path.of("data/channel.ser");
    private Map<UUID, Channel> data;

    private FileChannelRepository() {
        loadFile();
    }

    public static synchronized FileChannelRepository getInstance() {
        if (instance == null) {
            instance = new FileChannelRepository();
        }
        return instance;
    }

    // File > Object
    private void loadFile() {
        if (!Files.exists(path)) {
            data = new HashMap<>();

            return;
        }

        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            Object obj = ois.readObject();
            data = (Map<UUID, Channel>) obj;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Object > File
    private void saveFile() {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Channel save(Channel newChannel) {
        data.put(newChannel.getId(), newChannel);

        saveFile();
        return newChannel;
    }

    @Override
    public Channel findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel modify(Channel updatedChannel) {
        data.put(updatedChannel.getId(), updatedChannel);

        saveFile();
        return updatedChannel;
    }

    @Override
    public Channel deleteById(UUID id) {
        Channel findChannel = data.get(id);
        data.remove(id);

        saveFile();
        return findChannel;
    }
}
