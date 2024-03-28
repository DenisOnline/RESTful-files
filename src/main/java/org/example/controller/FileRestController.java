package org.example.controller;

import lombok.AllArgsConstructor;
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
        boolean result = fileService.rename(sourceName, targetName);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/copy")
    public ResponseEntity<Void> copyFile(@RequestParam("source") String sourceName, @RequestParam("target") String targetName) {
        boolean result = fileService.copy(sourceName, targetName);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFile(@RequestParam("filename") String filename) {
        boolean result = fileService.delete(filename);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filename") String filename) {
        byte[] fileContent = fileService.download(filename);
        if (fileContent != null) {
            return ResponseEntity.ok().body(fileContent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadFile(@RequestParam("filename") String filename, @RequestBody byte[] fileContent) {
        boolean result = fileService.upload(filename, fileContent);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

