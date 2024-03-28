package org.example.service.impl;

import org.example.model.FileDTO;
import org.example.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private final String BASE_PATH = "C:\\Users\\Шульженко Денис\\Desktop"; // Путь к папке с файлами

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
    public ResponseEntity<Void> rename(String sourceName, String targetName) {
        Path sourcePath = Paths.get(BASE_PATH, sourceName);
        Path targetPath = Paths.get(BASE_PATH, targetName);

        try {
            Files.move(sourcePath, targetPath);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> copy(String sourceName, String targetName) {
        Path sourcePath = Paths.get(BASE_PATH, sourceName);
        Path targetPath = Paths.get(BASE_PATH, targetName);

        try {
            Files.copy(sourcePath, targetPath);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> delete(String filename) {
        Path filePath = Paths.get(BASE_PATH, filename);

        try {
            Files.delete(filePath);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<byte[]> download(String filename) {
        Path filePath = Paths.get(BASE_PATH, filename);

        try {
            byte[] fileContent = Files.readAllBytes(filePath);
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"");
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> upload(String filename, byte[] fileContent) {
        try (FileOutputStream outputStream = new FileOutputStream(BASE_PATH + "/" + filename)) {
            outputStream.write(fileContent);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
