package com.buihuuduy.book_rating.mapper;

import com.buihuuduy.book_rating.DTO.request.CategoryRequest;
import com.buihuuduy.book_rating.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper
{
    CategoryEntity toCategoryEntity(CategoryRequest categoryRequest);
}
