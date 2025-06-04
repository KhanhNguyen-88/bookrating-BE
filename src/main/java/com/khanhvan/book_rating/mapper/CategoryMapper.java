package com.khanhvan.book_rating.mapper;

import com.khanhvan.book_rating.DTO.request.CategoryRequest;
import com.khanhvan.book_rating.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper
{
    CategoryEntity toCategoryEntity(CategoryRequest categoryRequest);
}
