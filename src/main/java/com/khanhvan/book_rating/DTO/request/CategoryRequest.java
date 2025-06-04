package com.khanhvan.book_rating.DTO.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest
{
    Long id;
    String cateName;

    String cateDescription;

    String cateImage;
}
