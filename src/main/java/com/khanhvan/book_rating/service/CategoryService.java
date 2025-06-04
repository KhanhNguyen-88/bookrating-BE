package com.khanhvan.book_rating.service;

import com.khanhvan.book_rating.DTO.request.CategoryRequest;
import com.khanhvan.book_rating.DTO.response.CategoryChart;
import com.khanhvan.book_rating.entity.CategoryEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface CategoryService
{
    List<CategoryEntity> getAllCategories();

    List<CategoryEntity> getCategoryById(long id);

    void addCategory(CategoryRequest categoryRequest);

    List<CategoryChart> displayCategoryChart();
    int updateCategory(CategoryRequest categoryRequest);
    CategoryEntity getCategory(int id);
}
