package com.buihuuduy.book_rating.service;

import com.buihuuduy.book_rating.DTO.request.CategoryRequest;
import com.buihuuduy.book_rating.entity.LanguageEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface LanguageService
{
    List<LanguageEntity> getAllLanguage();

    void addLanguage(String language);
}
