package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.entity.User;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.mapper.UserMapper;
import org.bekoocinema.request.auth.ChangePasswordRequest;
import org.bekoocinema.request.auth.RegisterUserRequest;
import org.bekoocinema.request.auth.SignInRequest;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.AuthenticationService;
import org.bekoocinema.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    final UserService userService;
    final AuthenticationService authenticationService;
    final UserMapper userMapper;

    @PostMapping("/register")
    public ApiResponse registerUser(@RequestBody @Valid RegisterUserRequest registerUserRequest) {
        userService.register(registerUserRequest);
        return ApiResponse.success(201, "Tạo tài khoản thành công");
    }

    @PostMapping("/otp-sign-in")
    public ApiResponse signIn(@RequestBody SignInRequest signInRequest) {
        return ApiResponse.success(200, "Vui lòng nhập mã xác thực được gửi về mail", authenticationService.getOtpSignIn(signInRequest));
    }

    @Operation(summary = "Xác thực OTP khi đăng nhập hoăc reset pw", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @PostMapping("/verify/{OTP}")
    public ApiResponse verify(@PathVariable String OTP, @AuthenticationPrincipal User user){
        return ApiResponse.success(200, "Xác thực OTP thành công (đăng nhập || reset pw)", authenticationService.verifyOtp(OTP, user));
    }

    @Operation(summary = "Lấy thông tin tài khoản bẳng token", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @PostMapping(value = "/token")
    public ApiResponse getUserByToken(@AuthenticationPrincipal User user) {
        return ApiResponse.success(200, "Lấy thông tin user đang đăng nhập", userMapper.toUserResponse(user));
    }

    @PostMapping("/forgot-password")
    public ApiResponse forgotPassword(@RequestParam String email) {
        return ApiResponse.success(200, "Vui lòng nhập mã xác thực được gửi về mail",
                authenticationService.getOtpForgotPassword(email));
    }


    @Operation(summary = "Đổi mật khẩu mới sau khi xác thực OTP", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @PostMapping("/reset-password")
    public ApiResponse resetPassword(@RequestBody @Valid ChangePasswordRequest request,
            @AuthenticationPrincipal User user) throws AppException {
        authenticationService.changePasswordNoAuth(request.getNewPassword(), user);
        return ApiResponse.success(200, "Đổi mật khẩu thành công");
    }

}
