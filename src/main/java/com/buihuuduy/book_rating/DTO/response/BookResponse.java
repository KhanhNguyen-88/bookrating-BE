package com.buihuuduy.book_rating.DTO.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
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

    Integer isFavorite;

    String createdBy; // Full Name

    String userImage;

    public BookResponse(Integer id, String bookName, String bookDescription, String bookImage, Date publishedDate, String bookFormat, String bookSaleLink, String language, String bookAuthor, String categoryName, Date createdAt, String createdBy, String userImage) {
        this.id = id;
        this.bookName = bookName;
        this.bookDescription = bookDescription;
        this.bookImage = bookImage;
        this.publishedDate = publishedDate;
        this.bookFormat = bookFormat;
        this.bookSaleLink = bookSaleLink;
        this.language = language;
        this.bookAuthor = bookAuthor;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.userImage = userImage;
    }
}
