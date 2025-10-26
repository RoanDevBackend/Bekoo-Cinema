package org.bekoocinema.service;

import org.bekoocinema.exception.AppException;
import org.bekoocinema.request.auth.RegisterUserRequest;
import org.bekoocinema.request.auth.UpdateUserRequest;
import org.bekoocinema.response.PageResponse;
import org.bekoocinema.response.user.UserResponse;

public interface UserService {
    void register(RegisterUserRequest registerUserRequest);
    PageResponse<UserResponse> getAllUsers(String key, int pageIndex, int pageSize);
    UserResponse update(String id, UpdateUserRequest updateUserRequest) throws AppException;
    void delete(String id) throws AppException;
}
