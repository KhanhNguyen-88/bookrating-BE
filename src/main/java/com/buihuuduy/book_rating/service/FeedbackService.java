package com.buihuuduy.book_rating.service;

import com.buihuuduy.book_rating.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService
{
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }


}
