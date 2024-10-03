package com.buihuuduy.book_rating.controller;

import com.buihuuduy.book_rating.DTO.ApiResponse;
import com.buihuuduy.book_rating.DTO.request.UserEntityRequest;
import com.buihuuduy.book_rating.exception.SuccessCode;
import com.buihuuduy.book_rating.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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

}
