package org.bekoocinema.service;

import org.bekoocinema.request.auth.RegisterUserRequest;
import org.bekoocinema.response.PageResponse;
import org.bekoocinema.response.user.UserResponse;

public interface UserService {
    void register(RegisterUserRequest registerUserRequest);
    PageResponse<UserResponse> getAllUsers(String key, int pageIndex, int pageSize);
}
