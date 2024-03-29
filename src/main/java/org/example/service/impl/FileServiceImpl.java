package org.example.service.impl;

import io.micrometer.common.util.StringUtils;
import org.example.model.FileDTO;
import org.example.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {
    @Value("${dir.path}")
    private String BASE_PATH;

    @Override
    public List<FileDTO> list(String directory) {
        Path path = Paths.get(BASE_PATH, directory);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return Collections.emptyList();
        }
        try (Stream<Path> stream = Files.list(path)) {
            return stream.map(file -> {
                FileDTO fileDTO = new FileDTO();
                fileDTO.setName(file.getFileName().toString());
                return fileDTO;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public boolean rename(String sourceName, String targetName) {
        if (StringUtils.isEmpty(sourceName) || StringUtils.isEmpty(targetName)) {
            return false;
        }

        try {
            Path sourcePath = Paths.get(BASE_PATH, sourceName);
            Path targetPath = Paths.get(BASE_PATH, targetName);
            Files.move(sourcePath, targetPath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean copy(String sourceName, String targetName) {
        if (StringUtils.isEmpty(sourceName) || StringUtils.isEmpty(targetName)) {
            return false;
        }

        try {
            Path sourcePath = Paths.get(BASE_PATH, sourceName);
            Path targetPath = Paths.get(BASE_PATH, targetName);
            Files.copy(sourcePath, targetPath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String filename) {
        if (StringUtils.isEmpty(filename)) {
            return false;
        }

        try {
            Path filePath = Paths.get(BASE_PATH, filename);
            Files.delete(filePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public byte[] download(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }

        try {
            Path filePath = Paths.get(BASE_PATH, filename);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean upload(String filename, byte[] fileContent) {
        if (StringUtils.isEmpty(filename) || fileContent == null || fileContent.length == 0) {
            return false;
        }

        try {
            Path filePath = Paths.get(BASE_PATH, filename);
            Files.write(filePath, fileContent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
