package org.example.service;

import org.example.model.FileDTO;

import java.util.List;

public interface FileService {
    List<FileDTO> list(String directory);
    boolean rename(String sourceName, String targetName);
    boolean copy(String sourceName, String targetName);
    boolean delete(String filename);
    byte[] download(String filename);
    boolean upload(String filename, byte[] fileContent);
}
