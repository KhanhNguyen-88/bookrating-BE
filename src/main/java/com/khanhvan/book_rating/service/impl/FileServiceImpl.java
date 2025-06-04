package com.khanhvan.book_rating.service.impl;

import com.khanhvan.book_rating.exception.CustomException;
import com.khanhvan.book_rating.exception.ErrorCode;
import com.khanhvan.book_rating.service.FileService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@Slf4j
public class FileServiceImpl implements FileService
{
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public FileServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String uploadFileWithMinio(MultipartFile multipartFile) {
        try {
            File file = convertMultipartFileToFile(multipartFile);

            String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
            String originalFileName = multipartFile.getOriginalFilename();
            String fileName = timestamp + "_" + originalFileName;

            InputStream inputStream = multipartFile.getInputStream();

            // Upload file to MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, multipartFile.getSize(), -1)
                            .build()
            );

            // Xóa file tạm
            if (!file.delete()) {
                log.warn("Failed to delete temporary file: {}", file.getAbsolutePath());
            }
            log.info("File uploaded successfully to MinIO: {}", fileName);
            return fileName;
        } catch (MinioException e) {
            log.error("MinIO error: {}", e.getMessage());
            throw new RuntimeException("MinIO error: " + e.getMessage());
        } catch (IOException e) {
            log.error("I/O error: {}", e.getMessage());
            throw new RuntimeException("I/O error: " + e.getMessage());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFileUrl(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .method(Method.GET)
                            .expiry(60 * 60)
                            .build()
            );
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException
    {
        File convertedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new CustomException(ErrorCode.DONT_CONVERT_MULTIPART);
        }
        return convertedFile;
    }
}
