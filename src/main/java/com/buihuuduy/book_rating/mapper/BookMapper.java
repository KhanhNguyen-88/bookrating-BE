package com.buihuuduy.book_rating.mapper;

import com.buihuuduy.book_rating.DTO.response.BookDetailResponse;
import com.buihuuduy.book_rating.entity.BookEntity;
import org.mapstruct.Mapper;

import java.awt.print.Book;

@Mapper(componentModel = "spring")
public interface BookMapper
{
    BookDetailResponse toBookDetailResponse(BookEntity bookEntity);
}
