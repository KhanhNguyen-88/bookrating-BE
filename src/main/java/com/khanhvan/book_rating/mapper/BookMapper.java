package com.khanhvan.book_rating.mapper;

import com.khanhvan.book_rating.DTO.request.BookRequestDTO;
import com.khanhvan.book_rating.DTO.response.BookResponse;
import com.khanhvan.book_rating.entity.BookEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper
{
    BookResponse toBookResponse(BookEntity bookEntity);
    BookEntity toBookEntity(BookRequestDTO bookRequestDTO);
}
