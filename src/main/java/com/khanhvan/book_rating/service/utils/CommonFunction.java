package com.khanhvan.book_rating.service.utils;

import com.khanhvan.book_rating.exception.CustomException;
import com.khanhvan.book_rating.exception.ErrorCode;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;

import java.text.ParseException;

public class CommonFunction
{
    public static String getUsernameFromToken(String token) {
        try {
            JWT jwt = JWTParser.parse(token);
            return jwt.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new CustomException(ErrorCode.JWT_INVALID);
        }
    }
}
