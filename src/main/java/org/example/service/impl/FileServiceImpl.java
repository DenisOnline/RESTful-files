package org.example.service.impl;

import io.micrometer.common.util.StringUtils;
import org.example.model.FileDTO;
import org.example.service.FileService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private final String BASE_PATH = "C:\\Users\\Шульженко Денис\\Desktop";

    @Override
    public List<FileDTO> list(String directory) {
        List<FileDTO> fileList = new ArrayList<>();
        String path = BASE_PATH + "/" + directory;
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            return fileList;
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                FileDTO fileDTO = new FileDTO();
                fileDTO.setName(file.getName());
                fileList.add(fileDTO);
            }
        }
        return fileList;
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
