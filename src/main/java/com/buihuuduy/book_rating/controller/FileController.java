package com.buihuuduy.book_rating.controller;

import com.buihuuduy.book_rating.DTO.ApiResponse;
import com.buihuuduy.book_rating.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController
{
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ApiResponse<String> uploadMultipleFilesWithMinIO (@RequestPart("file") MultipartFile file) throws IOException
    {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.result(fileService.uploadFileWithMinio(file));
        return apiResponse;
    }

    @GetMapping("/preview/{fileName}")
    public ApiResponse<String> previewImage(@PathVariable String fileName)
    {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(fileService.getFileUrl(fileName));
        return apiResponse;
    }
}
