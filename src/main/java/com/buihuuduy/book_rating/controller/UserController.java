package com.buihuuduy.book_rating.controller;

import com.buihuuduy.book_rating.DTO.ApiResponse;
import com.buihuuduy.book_rating.DTO.request.UserEntityRequest;
import com.buihuuduy.book_rating.DTO.response.AccountResponse;
import com.buihuuduy.book_rating.DTO.response.UserDetailResponse;
import com.buihuuduy.book_rating.exception.SuccessCode;
import com.buihuuduy.book_rating.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController
{
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/update/{userId}")
    public ApiResponse<?> registerUser(@RequestBody @Valid UserEntityRequest user, @PathVariable int userId)
    {
        userService.updateUser(userId, user);
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage(SuccessCode.UPDATE_SUCCESSFULLY.getMessage());
        return apiResponse;
    }

    @GetMapping("/detail/{userId}")
    public ApiResponse<UserDetailResponse> getUserDetailInfo(@PathVariable int userId)
    {
        ApiResponse<UserDetailResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(userService.getUserDetailInfo(userId));
        return apiResponse;
    }

    @GetMapping("/following-account/{userId}")
    public ApiResponse<List<AccountResponse>> getFollowingAccountByUser(@PathVariable int userId)
    {
        ApiResponse<List<AccountResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(userService.getFollowingAccountByUser(userId));
        return apiResponse;
    }

    @GetMapping("/follower-account/{userId}")
    public ApiResponse<List<AccountResponse>> getFollowerAccountByUser(@PathVariable int userId)
    {
        ApiResponse<List<AccountResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(userService.getFollowerAccountByUser(userId));
        return apiResponse;
    }
}
