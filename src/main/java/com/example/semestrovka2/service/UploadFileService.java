package com.example.semestrovka2.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileService {

    private final String folder = "upload-dir/";

    public String saveImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(folder + uniqueFileName);
            // Ensure the directory exists
            if (Files.notExists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, bytes);
            return uniqueFileName;
        }
        return "default.jpg";
    }

    public void deleteImage(String nombre) {
        String ruta = "upload-dir/";
        File file = new File(ruta + nombre);
        if (file.exists()) {
            file.delete();
        }
    }
}
