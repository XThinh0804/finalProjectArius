package vn.arius.finalProject.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface S3Service {
    String uploadFile(MultipartFile file, String folder);
    void deleteAvatarFile(String fileName);
}
