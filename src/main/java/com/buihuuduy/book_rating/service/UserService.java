package com.buihuuduy.book_rating.service;

import com.buihuuduy.book_rating.DTO.request.UserEntityRequest;
import com.buihuuduy.book_rating.entity.UserEntity;
import com.buihuuduy.book_rating.exception.CustomException;
import com.buihuuduy.book_rating.exception.ErrorCode;
import com.buihuuduy.book_rating.mapper.UserMapper;
import com.buihuuduy.book_rating.repository.UserRepository;
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

    public void updateUser(int userId, UserEntityRequest userEntityRequest)
    {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(userEntity, userEntityRequest);
        userRepository.save(userEntity);
    }
}
