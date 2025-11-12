package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.entity.User;
import org.bekoocinema.request.user.ProfileUpdateRequest;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    @Operation(
        summary = "Cập nhật thông tin cá nhân",
        security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @PutMapping("/user/profile")
    public ApiResponse updateProfile(@RequestBody @Valid ProfileUpdateRequest profileUpdateRequest, @AuthenticationPrincipal User user) {
        return ApiResponse.success(200, "Cập nhật thông tin thành công", userService.updateProfile(profileUpdateRequest, user));
    }
}
