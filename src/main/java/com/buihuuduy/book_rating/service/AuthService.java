package com.buihuuduy.book_rating.service;

import com.buihuuduy.book_rating.DTO.request.IntrospectRequest;
import com.buihuuduy.book_rating.DTO.request.UserEntityRequest;
import com.buihuuduy.book_rating.DTO.response.AuthenticationResponse;
import com.nimbusds.jose.JOSEException;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface AuthService
{
    void registerUser(UserEntityRequest userSignInRequest);

    AuthenticationResponse loginUser(UserEntityRequest userLoginRequest);

    boolean introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;
}
