package com.buihuuduy.book_rating.controller;

import com.buihuuduy.book_rating.DTO.ApiResponse;
import com.buihuuduy.book_rating.DTO.request.UserEntityRequest;
import com.buihuuduy.book_rating.exception.ErrorCode;
import com.buihuuduy.book_rating.exception.SuccessCode;
import com.buihuuduy.book_rating.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController
{
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponse<?> registerUser(@RequestBody @Valid UserEntityRequest user)
    {
        userService.registerUser(user);
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage(SuccessCode.REGISTER_SUCCESSFULLY.getMessage());
        return apiResponse;
    }

    @PostMapping("/login")
    public ApiResponse<?> loginUser(@RequestBody @Valid UserEntityRequest user)
    {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        if(!userService.canAuthenticated(user)) {
            apiResponse.setCode(401);
            apiResponse.setMessage(ErrorCode.LOGIN_UNSUCCESSFULLY.getMessage());
        }
        else {
            apiResponse.setCode(200);
            apiResponse.setMessage(SuccessCode.LOGIN_SUCCESSFULLY.getMessage());
        }
        return apiResponse;
    }

}
