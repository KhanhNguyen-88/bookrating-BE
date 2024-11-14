package com.buihuuduy.book_rating.service;

import com.buihuuduy.book_rating.entity.CategoryEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface CategoryService
{
    List<CategoryEntity> getAllCategories();

    List<CategoryEntity> getCategoryById(long id);
}
