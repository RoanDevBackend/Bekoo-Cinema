package org.bekoocinema.service;

import org.bekoocinema.request.auth.RegisterUserRequest;

public interface UserService {
    void register(RegisterUserRequest registerUserRequest);
}
