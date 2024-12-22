package com.buihuuduy.book_rating.service.impl;

import com.buihuuduy.book_rating.DTO.request.CategoryRequest;
import com.buihuuduy.book_rating.DTO.response.CategoryChart;
import com.buihuuduy.book_rating.DTO.response.CommentResponse;
import com.buihuuduy.book_rating.entity.CategoryEntity;
import com.buihuuduy.book_rating.mapper.CategoryMapper;
import com.buihuuduy.book_rating.repository.BookCategoryRepository;
import com.buihuuduy.book_rating.repository.BookRepository;
import com.buihuuduy.book_rating.repository.CategoryRepository;
import com.buihuuduy.book_rating.service.CategoryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CategoryServiceImpl implements CategoryService
{
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, BookRepository bookRepository, BookCategoryRepository bookCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.bookRepository = bookRepository;
        this.bookCategoryRepository = bookCategoryRepository;
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

    @Override
    public List<CategoryChart> displayCategoryChart()
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bc.category_id, c.cate_name, COUNT(bc.book_id) AS book_quantity ")
                .append("FROM book_category bc JOIN category c ON bc.category_id = c.id ")
                .append("GROUP BY bc.category_id LIMIT 10");

        Query query = entityManager.createNativeQuery(sql.toString());

        List<Object[]> results = query.getResultList();
        List<CategoryChart> categoryChartList = new ArrayList<>();
        Integer sumBookQuantity = bookRepository.countBook();
        for (Object[] result : results) {
            CategoryChart categoryChart = new CategoryChart();
            categoryChart.setCategoryName((String) result[1]);
            categoryChart.setBookQuantity((Long) result[2]);
            Double percent = ((Long) result[2] * 100.00) / sumBookQuantity;
            categoryChart.setPercent(Double.parseDouble(String.format(Locale.US,"%.2f", percent)));
            categoryChartList.add(categoryChart);
        }
        return categoryChartList;
    }
}
