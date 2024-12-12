package com.buihuuduy.book_rating.service.impl;

import com.buihuuduy.book_rating.DTO.request.IntrospectRequest;
import com.buihuuduy.book_rating.DTO.request.UserEntityRequest;
import com.buihuuduy.book_rating.DTO.response.AuthenticationResponse;
import com.buihuuduy.book_rating.entity.UserEntity;
import com.buihuuduy.book_rating.exception.CustomException;
import com.buihuuduy.book_rating.exception.ErrorCode;
import com.buihuuduy.book_rating.mapper.UserMapper;
import com.buihuuduy.book_rating.repository.UserRepository;
import com.buihuuduy.book_rating.service.AuthService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nimbusds.jwt.JWTClaimsSet;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService
{
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${jwt.signerKey}")
    private String signerKey;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void registerUser(UserEntityRequest userSignInRequest)
    {
        if(userSignInRequest.getUsername().isEmpty() || userRepository.existsByUsername(userSignInRequest.getUsername())) {
            throw new CustomException(ErrorCode.USERNAME_ALREADY_EXIST);
        }
        if(userSignInRequest.getEmail().isEmpty() || userRepository.existsByUserEmail(userSignInRequest.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXIST);
        }

        UserEntity userEntity = userMapper.toUser(userSignInRequest);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setIsAdmin(false);

        // Encode password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        userEntity.setPassword(passwordEncoder.encode(userSignInRequest.getPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public AuthenticationResponse loginUser(UserEntityRequest userLoginRequest)
    {
        var userEntity = userRepository.findByUsername(userLoginRequest.getUsername());
        if(userEntity == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean isAuthenticated =  passwordEncoder.matches(userLoginRequest.getPassword(), userEntity.getPassword());

        if(!isAuthenticated) {
            throw new CustomException(ErrorCode.LOGIN_UNSUCCESSFULLY);
        }

        String token = generateToken(userLoginRequest);

        return AuthenticationResponse.builder().token(token).isAdmin(userEntity.getIsAdmin()).build();
    }

    @Override
    // Verify token
    public boolean introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException
    {
        var token = introspectRequest.getToken();
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        return (verified && expiryTime.after(new Date()));
    }

    private String generateToken(UserEntityRequest userRequest) {
        // Header of JWT
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userRequest.getUsername())
                .issuer("buihuuduy.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()))
                .claim("customClaim", "Custom Claim")
                .build();

        // Payload of JWT

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

    }
}
