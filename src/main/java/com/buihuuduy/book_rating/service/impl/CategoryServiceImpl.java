package com.buihuuduy.book_rating.service.impl;

import com.buihuuduy.book_rating.DTO.request.CategoryRequest;
import com.buihuuduy.book_rating.entity.CategoryEntity;
import com.buihuuduy.book_rating.mapper.CategoryMapper;
import com.buihuuduy.book_rating.repository.CategoryRepository;
import com.buihuuduy.book_rating.service.CategoryService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService
{
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<CategoryEntity> getCategoryById(long id) {
        return categoryRepository.findCategoriesByBookId(id);
    }

    @Override
    public void addCategory(CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = categoryMapper.toCategoryEntity(categoryRequest);
        categoryRepository.save(categoryEntity);
    }
}
