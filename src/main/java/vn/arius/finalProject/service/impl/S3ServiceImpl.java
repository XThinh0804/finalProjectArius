package vn.arius.finalProject.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.arius.finalProject.service.S3Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 amazonS3;

    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public File convertMultiPartToFile(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
        return convFile;
    }

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        File localFile = null;
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String fileName = folder + "/" + currentDateTime + file.getOriginalFilename();
        localFile = this.convertMultiPartToFile(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, localFile)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        this.amazonS3.putObject(putObjectRequest);
        if (localFile.exists()) {
            localFile.delete();
        }
        String fileUrl = this.amazonS3.getUrl(bucketName, fileName).toString();
        return fileUrl;
    }

    @Override
    public void deleteAvatarFile(String fileName) {
        try {
            String decodedFile = URLDecoder.decode(fileName, StandardCharsets.UTF_8.toString());
            String key = fileName.substring(decodedFile.indexOf("avatar/"));
            this.amazonS3.deleteObject(bucketName, key);
        } catch (Exception e) {
            System.err.println("Error delete file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
