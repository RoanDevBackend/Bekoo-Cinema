package org.bekoocinema.service;

import org.bekoocinema.entity.User;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.request.auth.RegisterUserRequest;
import org.bekoocinema.request.user.ChangePasswordAuthRequest;
import org.bekoocinema.request.user.ProfileUpdateRequest;
import org.bekoocinema.response.user.UserResponse;

public interface UserService {
    void register(RegisterUserRequest registerUserRequest);
    UserResponse updateProfile(ProfileUpdateRequest profileUpdateRequest, User user);
    void changePassword(ChangePasswordAuthRequest request, User user)
        throws AppException;
}
