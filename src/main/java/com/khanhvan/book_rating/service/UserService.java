package com.khanhvan.book_rating.service;

import com.khanhvan.book_rating.DTO.request.UserInfoRequest;
import com.khanhvan.book_rating.DTO.response.AccountResponse;
import com.khanhvan.book_rating.DTO.response.UserDetailResponse;
import com.khanhvan.book_rating.DTO.response.UserInfoResponse;
import com.khanhvan.book_rating.entity.UserEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService
{
    Integer getIdByToken(String token);

    void updateUser(Integer userId, UserInfoRequest userInfoRequest);

    UserInfoResponse getUserInfoByToken(String token);

    List<AccountResponse> getFollowingAccountByUser(String token, Integer userId);

    List<AccountResponse> getFollowerAccountByUser(String token, Integer userId);

    UserDetailResponse getUserDetailInfo(String token, Integer userId);

    void followUser(String token, Integer followedId);

    void unfollowUser(String token, Integer unFollowedId);

    void markAsFavorite(String token, Integer bookId);

    void unMarkFavorites(String token, Integer bookId);

    List<AccountResponse> getFollowingAccountByToken(String token);

    List<AccountResponse> getFollowerAccountByToken(String token);

    UserDetailResponse getUserDetailInfoByToken(String token);

    List<UserEntity> getAllUser();

    void deleteUser(Integer userId);

    UserInfoResponse getUserInfoById(Integer userId);


}
