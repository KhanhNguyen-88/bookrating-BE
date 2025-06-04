package com.khanhvan.book_rating.service;

import com.khanhvan.book_rating.entity.LanguageEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface LanguageService
{
    List<LanguageEntity> getAllLanguage();

    void addLanguage(String language);
}
