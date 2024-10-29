package com.buihuuduy.book_rating.service.impl;

import com.buihuuduy.book_rating.exception.CustomException;
import com.buihuuduy.book_rating.exception.ErrorCode;
import com.buihuuduy.book_rating.service.MailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final ConcurrentHashMap<String, String> currentCode = new ConcurrentHashMap<>();

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(String toEmail)
    {
        String subject = "MÃ XÁC THỰC BookRating website";
        String code = generateVerificationCode();
        String body = "HELLO " + toEmail + "\n\n" + code + " LÀ MÃ XÁC MINH CỦA BẠN.";
        currentCode.put(toEmail, code);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Override
    public void verifyCode(String email, String code)
    {
        String storedCode = currentCode.get(email);
        if(!storedCode.equals(code)) {
            throw new CustomException(ErrorCode.VERIFICATION_CODE_INVALID);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000)); // Mã 6 chữ số
    }
}