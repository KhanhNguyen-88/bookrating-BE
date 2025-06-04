package com.khanhvan.book_rating.service;

import org.springframework.stereotype.Service;

@Service
public interface MailService
{
    void sendMail(String toEmail);

    void verifyCode(String email, String code);
}
