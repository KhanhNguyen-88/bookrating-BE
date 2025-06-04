package com.khanhvan.book_rating.DTO.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryChart
{
    String categoryName;

    Double percent; // Percent cate's appearance in total book

    Long bookQuantity; // Total books have this cate
}
