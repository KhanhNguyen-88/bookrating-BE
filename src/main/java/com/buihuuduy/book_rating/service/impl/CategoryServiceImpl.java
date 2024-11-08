package com.buihuuduy.book_rating.service.impl;

import com.buihuuduy.book_rating.entity.CategoryEntity;
import com.buihuuduy.book_rating.repository.CategoryRepository;
import com.buihuuduy.book_rating.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<CategoryEntity> getCategoryById(long id) {
        return categoryRepository.findCategoriesByBookId(id);
    }
}
