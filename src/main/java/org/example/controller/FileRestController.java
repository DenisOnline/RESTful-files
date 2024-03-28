package org.example.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.model.FileDTO;
import org.example.service.impl.FileServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
@AllArgsConstructor
public class FileRestController {
    private final FileServiceImpl fileService;
    @GetMapping
    public ResponseEntity<List<String>> listFiles(@RequestParam(value = "directory", required = false) String directory) {
        List<FileDTO> fileList = fileService.list(directory);
        if (fileList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<String> fileNames = fileList.stream().map(FileDTO::getName).collect(Collectors.toList());
        return new ResponseEntity<>(fileNames, HttpStatus.OK);
    }

    @PutMapping("/rename")
    public ResponseEntity<Void> renameFile(@RequestParam("source") String sourceName, @RequestParam("target") String targetName) {
        ResponseEntity<Void> response = fileService.rename(sourceName, targetName);
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @PostMapping("/copy")
    public ResponseEntity<Void> copyFile(@RequestParam("source") String sourceName, @RequestParam("target") String targetName) {
        return fileService.copy(sourceName, targetName);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFile(@RequestParam("filename") String filename) {
        return fileService.delete(filename);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filename") String filename) {
        return fileService.download(filename);
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadFile(@RequestParam("filename") String filename, @RequestBody byte[] fileContent) {
        return fileService.upload(filename, fileContent);
    }
}

