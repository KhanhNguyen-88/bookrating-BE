package com.khanhvan.book_rating.controller;

import com.khanhvan.book_rating.DTO.ApiResponse;
import com.khanhvan.book_rating.DTO.response.CategoryChart;
import com.khanhvan.book_rating.service.CategoryService;
import com.khanhvan.book_rating.DTO.request.CategoryRequest;
import com.khanhvan.book_rating.entity.CategoryEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController
{
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/get-all")
    public ApiResponse<List<CategoryEntity>> getAllCategories() {
        return new ApiResponse<>().result(categoryService.getAllCategories());
    }

    @GetMapping("/get-by-book")
    public ApiResponse<List<CategoryEntity>> getByBookId(@RequestParam int bookId) {
        return new ApiResponse<>().result(categoryService.getCategoryById(bookId));
    }

    @PostMapping("/add")
    public ApiResponse<?> addCategory(@RequestBody CategoryRequest categoryRequest)
    {
        categoryService.addCategory(categoryRequest);
        ApiResponse<?> response = new ApiResponse<>();
        response.setCode(200);
        return response;
    }

    @GetMapping("/chart")
    public ApiResponse<List<CategoryChart>> getCategoryChart()
    {
        ApiResponse<List<CategoryChart>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(categoryService.displayCategoryChart());
        return apiResponse;
    }
    @PostMapping("/update")
    public ApiResponse<?> updateCategory(@RequestBody CategoryRequest categoryRequest){
        categoryService.updateCategory(categoryRequest);
        ApiResponse<?> response = new ApiResponse<>();
        response.setCode(200);
        return response;
    }
    @GetMapping("/info-detail/{categoryId}")
    public ApiResponse<CategoryEntity> getDetailById(@PathVariable int categoryId){
        ApiResponse<CategoryEntity> response = new ApiResponse<CategoryEntity>();
        response.setCode(200);
        return response.result(categoryService.getCategory(categoryId));
    }
}
