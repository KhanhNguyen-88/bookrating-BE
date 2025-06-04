package com.khanhvan.book_rating.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService
{
    String uploadFileWithMinio(MultipartFile file);

    String getFileUrl(String fileName);

}
