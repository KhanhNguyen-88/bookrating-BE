package com.khanhvan.book_rating.DTO.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse
{
    String userImage;

    String userName;

    Integer userId;

    String comment;

    Integer rating;

    LocalDateTime createdAt;
}
