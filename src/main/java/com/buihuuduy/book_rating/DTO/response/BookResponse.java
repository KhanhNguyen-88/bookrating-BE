package com.buihuuduy.book_rating.DTO.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse
{
    Integer id;

    String bookName;

    String bookDescription;

    String bookImage;

    Date publishedDate;

    String bookFormat;

    String bookSaleLink;

    String language;

    String bookAuthor;

    String categoryName;

    Long averageRating;

    Long totalRating;

    Date createdAt;

}
