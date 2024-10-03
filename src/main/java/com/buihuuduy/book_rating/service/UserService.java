package com.buihuuduy.book_rating.service;

import com.buihuuduy.book_rating.DTO.request.UserEntityRequest;
import com.buihuuduy.book_rating.entity.UserEntity;
import com.buihuuduy.book_rating.exception.CustomException;
import com.buihuuduy.book_rating.exception.ErrorCode;
import com.buihuuduy.book_rating.mapper.UserMapper;
import com.buihuuduy.book_rating.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void registerUser(UserEntityRequest userSignInRequest)
    {
        if(userSignInRequest.getUsername().isEmpty() || userRepository.existsByUsername(userSignInRequest.getUsername())) {
            throw new CustomException(ErrorCode.USERNAME_ALREADY_EXIST);
        }
        if(userSignInRequest.getEmail().isEmpty() || userRepository.existsByUserEmail(userSignInRequest.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXIST);
        }

        UserEntity userEntity = userMapper.toUser(userSignInRequest);

        // Encode password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        userEntity.setPassword(passwordEncoder.encode(userSignInRequest.getPassword()));
        userRepository.save(userEntity);
    }

    public boolean canAuthenticated(UserEntityRequest userLoginRequest)
    {
        var userEntity = userRepository.findByUsername(userLoginRequest.getUsername());
        if(userEntity == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(userLoginRequest.getPassword(), userEntity.getPassword());
    }
}
