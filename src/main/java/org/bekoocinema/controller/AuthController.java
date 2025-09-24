package org.bekoocinema.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.entity.User;
import org.bekoocinema.request.auth.RegisterUserRequest;
import org.bekoocinema.request.auth.SignInRequest;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.AuthenticationService;
import org.bekoocinema.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    final UserService userService;
    final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApiResponse registerUser(@RequestBody @Valid RegisterUserRequest registerUserRequest) {
        userService.register(registerUserRequest);
        return ApiResponse.success(201, "Tạo tài khoản thành công");
    }

    @PostMapping("/otp-sign-in")
    public ApiResponse signIn(@RequestBody SignInRequest signInRequest) {
        return ApiResponse.success(200, "Vui lòng nhập mã xác thực được gửi về mail", authenticationService.getOtpSignIn(signInRequest));
    }

    @PostMapping("/verify-sign-in/{OTP}")
    public ApiResponse verifySignIn(@PathVariable String OTP, @AuthenticationPrincipal User user){
        return ApiResponse.success(200, "Đăng nhập thành công", authenticationService.verifyOtpSignIn(OTP, user));
    }

}
