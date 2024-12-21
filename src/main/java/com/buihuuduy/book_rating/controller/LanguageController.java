package com.buihuuduy.book_rating.controller;

import com.buihuuduy.book_rating.DTO.ApiResponse;
import com.buihuuduy.book_rating.entity.LanguageEntity;
import com.buihuuduy.book_rating.service.LanguageService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/language")
public class LanguageController
{
    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping("/get-all")
    public ApiResponse<List<LanguageEntity>> getAllLanguage() {
        return new ApiResponse<>().result(languageService.getAllLanguage());
    }

    @PostMapping("/add")
    public ApiResponse<?> addLanguage(@RequestBody Object language)
    {
        languageService.addLanguage(language.toString().substring(10, 15));
        ApiResponse<?> response = new ApiResponse<>();
        response.setCode(200);
        return response;
    }
}
