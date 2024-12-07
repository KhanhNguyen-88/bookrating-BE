package com.buihuuduy.book_rating.DTO.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequest
{
    String comment;

    Integer rating;

    Integer bookId;
}
