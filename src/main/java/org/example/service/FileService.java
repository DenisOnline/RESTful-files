package org.example.service;

import org.example.model.FileDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FileService {
    List<FileDTO> list(String directory);
    ResponseEntity<Void> rename(String sourceName, String targetName);
    ResponseEntity<Void> copy(String sourceName, String targetName);
    ResponseEntity<Void> delete(String filename);
    ResponseEntity<byte[]> download(String filename);
    ResponseEntity<Void> upload(String filename, byte[] fileContent);
}
