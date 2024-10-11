package com.buihuuduy.book_rating.mapper;

import com.buihuuduy.book_rating.DTO.response.BookDetailPageResponse;
import com.buihuuduy.book_rating.DTO.response.BookResponse;
import com.buihuuduy.book_rating.entity.BookEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper
{
    BookResponse toBookResponse(BookEntity bookEntity);
}
