package com.khanhvan.book_rating.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackRequest
{
    String userName;

    String comment;

    Integer rating;

    Integer bookId;
}
