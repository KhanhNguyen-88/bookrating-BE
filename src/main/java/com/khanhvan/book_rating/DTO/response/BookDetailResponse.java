package com.khanhvan.book_rating.DTO.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDetailResponse
{
    BookResponse bookResponse;

    List<CommentResponse> feedbackResponseList;

    List<PercentFeedback> percentFeedbackList;
}
