package com.khanhvan.book_rating.DTO.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequestDTO
{
    String bookName;

    String bookDescription;

    String bookImage;

    LocalDate publishedDate;

    String bookFormat;

    String bookSaleLink;

    Integer languageId;

    String bookAuthor;

    List<Integer> categoryId;
}
