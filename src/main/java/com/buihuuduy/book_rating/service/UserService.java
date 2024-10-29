package com.buihuuduy.book_rating.service;

import com.buihuuduy.book_rating.DTO.request.UserEntityRequest;
import com.buihuuduy.book_rating.DTO.response.AccountResponse;
import com.buihuuduy.book_rating.DTO.response.UserDetailResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService
{
    void updateUser(Integer userId, UserEntityRequest userEntityRequest);

    List<AccountResponse> getFollowingAccountByUser(Integer userId);

    List<AccountResponse> getFollowerAccountByUser(Integer userId);

    public UserDetailResponse getUserDetailInfo(Integer userId);
}
