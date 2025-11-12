package org.bekoocinema.service;

import org.bekoocinema.entity.User;
import org.bekoocinema.request.auth.RegisterUserRequest;
import org.bekoocinema.request.user.ProfileUpdateRequest;
import org.bekoocinema.response.user.UserResponse;

public interface UserService {
    void register(RegisterUserRequest registerUserRequest);
    // Có phải dùng AuthenticationPrincipal cho service này không? Service này được thực hiện khi user đã đăng nhập
    UserResponse updateProfile(ProfileUpdateRequest profileUpdateRequest, User user);
}
