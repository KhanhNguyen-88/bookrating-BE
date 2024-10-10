package com.buihuuduy.book_rating.controller;

import com.buihuuduy.book_rating.DTO.ApiResponse;
import com.buihuuduy.book_rating.DTO.request.IntrospectRequest;
import com.buihuuduy.book_rating.DTO.request.UserEntityRequest;
import com.buihuuduy.book_rating.DTO.response.AuthenticationResponse;
import com.buihuuduy.book_rating.exception.SuccessCode;
import com.buihuuduy.book_rating.service.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;

@RestController
@RequestMapping("/api/user")
public class AuthController
{
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<?> registerUser(@RequestBody @Valid UserEntityRequest user)
    {
        authService.registerUser(user);
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage(SuccessCode.REGISTER_SUCCESSFULLY.getMessage());
        return apiResponse;
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> loginUser(@RequestBody @Valid UserEntityRequest user)
    {
        AuthenticationResponse result = authService.loginUser(user);
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage(SuccessCode.LOGIN_SUCCESSFULLY.getMessage());
        apiResponse.setResult(result);
        return apiResponse;
    }

    @PostMapping("/introspect")
    ApiResponse<?> authenticate(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException
    {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        if(authService.introspect(introspectRequest)) {
            apiResponse.setCode(200);
            apiResponse.setMessage("Token successfully validated");
        }
        else {
            apiResponse.setCode(400);
            apiResponse.setMessage("Invalid token");
        }
        return apiResponse;
    }

//    @GetMapping("/get-current-user")
//    public ResponseEntity<String> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7); // Lấy token từ header
//            String username = authService.getUsernameFromToken(token); // Giải mã token và lấy subject
//            return ResponseEntity.ok("Current user: " + username);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token not provided");
//        }
//    }
}
