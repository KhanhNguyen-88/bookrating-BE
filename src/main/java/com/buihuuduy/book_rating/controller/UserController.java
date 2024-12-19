package com.buihuuduy.book_rating.controller;

import com.buihuuduy.book_rating.DTO.ApiResponse;
import com.buihuuduy.book_rating.DTO.request.UserInfoRequest;
import com.buihuuduy.book_rating.DTO.response.AccountResponse;
import com.buihuuduy.book_rating.DTO.response.UserDetailResponse;
import com.buihuuduy.book_rating.DTO.response.UserInfoResponse;
import com.buihuuduy.book_rating.entity.UserEntity;
import com.buihuuduy.book_rating.exception.SuccessCode;
import com.buihuuduy.book_rating.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user-token")
    public ApiResponse<UserInfoResponse> getUserOnDetailPage(@RequestHeader("Authorization") String authorizationHeader)
    {
        ApiResponse<UserInfoResponse> apiResponse = new ApiResponse<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            apiResponse.setResult(userService.getUserInfoByToken(token));
            apiResponse.setCode(200);
        } else {
            apiResponse.setCode(401);
            apiResponse.setMessage("Authorization header is invalid");
        }
        return apiResponse;
    }

    @PostMapping("/update/{userId}")
    public ApiResponse<?> registerUser(@RequestBody @Valid UserInfoRequest user, @PathVariable int userId) {
        userService.updateUser(userId, user);
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage(SuccessCode.UPDATE_SUCCESSFULLY.getMessage());
        return apiResponse;
    }

    // Show detail different profile by id
    @GetMapping("/detail/{userId}")
    public ApiResponse<UserDetailResponse> getUserDetailInfo(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int userId)
    {
        String token = authorizationHeader.substring(7);
        ApiResponse<UserDetailResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(userService.getUserDetailInfo(token, userId));
        return apiResponse;
    }

    @GetMapping("/following-account/{userId}")
    public ApiResponse<List<AccountResponse>> getFollowingAccountByUser(@PathVariable int userId) {
        ApiResponse<List<AccountResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(userService.getFollowingAccountByUser(userId));
        return apiResponse;
    }

    @GetMapping("/follower-account/{userId}")
    public ApiResponse<List<AccountResponse>> getFollowerAccountByUser(@PathVariable int userId) {
        ApiResponse<List<AccountResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(userService.getFollowerAccountByUser(userId));
        return apiResponse;
    }

    @PostMapping("/follow-account/{followedId}")
    public ApiResponse<?> followAccount(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Integer followedId) {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Lấy token từ header
            userService.followUser(token, followedId);
            apiResponse.setCode(200);
        } else {
            apiResponse.setCode(401);
            apiResponse.setMessage("Authorization header is invalid");
        }
        return apiResponse;
    }

    @PostMapping("/unfollow-account/{unfollowedId}")
    public ApiResponse<?> unFollowAccount(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Integer unfollowedId) {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Lấy token từ header
            userService.unfollowUser(token, unfollowedId);
            apiResponse.setCode(200);
        } else {
            apiResponse.setCode(401);
            apiResponse.setMessage("Authorization header is invalid");
        }
        return apiResponse;
    }

    @PostMapping("/mark-favorite-book/{bookId}")
    public ApiResponse<?> markAsFavorite(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Integer bookId) {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Lấy token từ header
            userService.markAsFavorite(token, bookId);
            apiResponse.setCode(200);
        } else {
            apiResponse.setCode(401);
            apiResponse.setMessage("Authorization header is invalid");
        }
        return apiResponse;
    }

    @PostMapping("/unmark-favorite-book/{bookId}")
    public ApiResponse<?> unmarkAsFavorite(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Integer bookId) {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Lấy token từ header
            userService.unMarkFavorites(token, bookId);
            apiResponse.setCode(200);
        } else {
            apiResponse.setCode(401);
            apiResponse.setMessage("Authorization header is invalid");
        }
        return apiResponse;
    }

    @GetMapping("/following-account-by-token")
    public ApiResponse<List<AccountResponse>> getFollowingAccountByToken(@RequestHeader("Authorization") String authorizationHeader) {
        ApiResponse<List<AccountResponse>> apiResponse = new ApiResponse<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            apiResponse.setResult(userService.getFollowingAccountByToken(token));
            apiResponse.setCode(200);
        } else {
            apiResponse.setCode(401);
            apiResponse.setMessage("Authorization header is invalid");
        }
        return apiResponse;
    }

    @GetMapping("/follower-account-by-token")
    public ApiResponse<List<AccountResponse>> getFollowerAccountByToken(@RequestHeader("Authorization") String authorizationHeader) {
        ApiResponse<List<AccountResponse>> apiResponse = new ApiResponse<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            apiResponse.setResult(userService.getFollowerAccountByToken(token));
            apiResponse.setCode(200);
        } else {
            apiResponse.setCode(401);
            apiResponse.setMessage("Authorization header is invalid");
        }
        return apiResponse;
    }

    @GetMapping("/detail-by-token")
    public ApiResponse<UserDetailResponse> getUserDetailInfoByToken(@RequestHeader("Authorization") String authorizationHeader) {
        ApiResponse<UserDetailResponse> apiResponse = new ApiResponse<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            apiResponse.setResult(userService.getUserDetailInfoByToken(token));
            apiResponse.setCode(200);
        } else {
            apiResponse.setCode(401);
            apiResponse.setMessage("Authorization header is invalid");
        }
        return apiResponse;
    }

    @GetMapping("get-id-by-token")
    public ApiResponse<Integer> getUserIdByToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        ApiResponse<Integer> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getIdByToken(token));
        apiResponse.setCode(200);
        return apiResponse;
    }

    @GetMapping("/get-all")
    public ApiResponse<List<UserEntity>> getAllUserOnAdminPage()
    {
        ApiResponse<List<UserEntity>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllUser());
        apiResponse.setCode(200);
        return apiResponse;
    }

    @GetMapping("/delete/{userId}")
    public ApiResponse<?> deleteUser(@PathVariable Integer userId)
    {
        userService.deleteUser(userId);
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Xóa người dùng thành công");
        apiResponse.setCode(200);
        return apiResponse;
    }

    // Admin check info detail each user
    @GetMapping("/info-detail/{userId}")
    public ApiResponse<UserInfoResponse> getUserDetailInfo(@PathVariable Integer userId)
    {
        ApiResponse<UserInfoResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(userService.getUserInfoById(userId));
        return apiResponse;
    }
}
