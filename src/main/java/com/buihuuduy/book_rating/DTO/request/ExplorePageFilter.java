package com.buihuuduy.book_rating.DTO.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExplorePageFilter
{
    // Name
    String bookAuthor;

    Integer categoryId;

    // AVG
    Integer rating;
}
