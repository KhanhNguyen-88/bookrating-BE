package com.khanhvan.book_rating.service.impl;

import com.khanhvan.book_rating.entity.LanguageEntity;
import com.khanhvan.book_rating.repository.LanguageRepository;
import com.khanhvan.book_rating.service.LanguageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService
{
    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public List<LanguageEntity> getAllLanguage() {
        return languageRepository.findAll();
    }

    @Override
    public void addLanguage(String language) {
        LanguageEntity languageEntity = new LanguageEntity();
        languageEntity.setLanguageName(language);
        languageRepository.save(languageEntity);
    }
}
