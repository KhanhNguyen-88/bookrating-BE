package com.buihuuduy.book_rating.DTO.response;

import com.buihuuduy.book_rating.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse extends AuditingEntity
{
    String bookName;

    String bookDescription;

    String bookImage;

    LocalDate publishedDate;

    String bookFormat;

    String bookSaleLink;

    String language;

    String bookAuthor;

    String categoryName;
}
