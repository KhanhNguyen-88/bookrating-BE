package com.buihuuduy.book_rating.service.impl;

import com.buihuuduy.book_rating.DTO.request.UserEntityRequest;
import com.buihuuduy.book_rating.DTO.response.AccountResponse;
import com.buihuuduy.book_rating.DTO.response.UserDetailResponse;
import com.buihuuduy.book_rating.entity.UserEntity;
import com.buihuuduy.book_rating.exception.CustomException;
import com.buihuuduy.book_rating.exception.ErrorCode;
import com.buihuuduy.book_rating.mapper.UserMapper;
import com.buihuuduy.book_rating.repository.FollowAccountRepository;
import com.buihuuduy.book_rating.repository.UserRepository;
import com.buihuuduy.book_rating.service.UserService;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService
{
    private final FollowAccountRepository followAccountRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, FollowAccountRepository followAccountRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.followAccountRepository = followAccountRepository;
    }

    @Override
    public void updateUser(Integer userId, UserEntityRequest userEntityRequest)
    {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(userEntity, userEntityRequest);
        userRepository.save(userEntity);
    }

    // Not done
    /*
    public void addFeeBack(String authorizationHeader, Integer bookId, String comment, Integer rating) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            Integer currentUsername =  getUserIdFromToken(token);
        }
    }
    */

    private String getUsernameFromToken(String token) {
        try {
            JWT jwt = JWTParser.parse(token);
            return jwt.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new CustomException(ErrorCode.JWT_INVALID);
        }
    }

    @Override
    public List<AccountResponse> getFollowingAccountByUser(Integer userId)
    {
        List<Integer> listFollowingAccountId = followAccountRepository.findAllFollowingAccountIdsByYourAccountId(userId);
        List<AccountResponse> accountResponseList = new ArrayList<>();
        for(Integer followingAccountId : listFollowingAccountId)
        {
            AccountResponse accountResponse = new AccountResponse();
            UserEntity userEntity = userRepository.findById(followingAccountId).orElseThrow();

            accountResponse.setUserId(userEntity.getId());
            accountResponse.setUserName(userEntity.getUsername());
            accountResponse.setUserImage(userEntity.getUserImage());

            accountResponseList.add(accountResponse);
        }
        return accountResponseList;
    }

    @Override
    public List<AccountResponse> getFollowerAccountByUser(Integer userId)
    {
        List<Integer> listFollowerAccountId = followAccountRepository.findAllFollowerAccountIdsByFollowingAccountId(userId);
        List<AccountResponse> accountResponseList = new ArrayList<>();
        for(Integer followingAccountId : listFollowerAccountId)
        {
            AccountResponse accountResponse = new AccountResponse();
            UserEntity userEntity = userRepository.findById(followingAccountId).orElseThrow();

            accountResponse.setUserId(userEntity.getId());
            accountResponse.setUserName(userEntity.getUsername());
            accountResponse.setUserImage(userEntity.getUserImage());

            accountResponseList.add(accountResponse);
        }
        return accountResponseList;
    }

    @Override
    // Xem thong tin ca nhan cua nguoi dung nao do
    public UserDetailResponse getUserDetailInfo(Integer userId)
    {
        UserDetailResponse userDetailResponse = new UserDetailResponse();
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        userDetailResponse.setUserName(userEntity.getUsername());
        userDetailResponse.setUserImage(userEntity.getUserImage());
        // Thieu so luong bai post
        userDetailResponse.setFollowingAccounts(getFollowingAccountByUser(userId).size());
        userDetailResponse.setFollowerAccounts(getFollowerAccountByUser(userId).size());
        return userDetailResponse;
    }
}
