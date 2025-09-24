package org.bekoocinema.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.entity.User;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.mapper.UserMapper;
import org.bekoocinema.repository.UserRepository;
import org.bekoocinema.request.auth.RegisterUserRequest;
import org.bekoocinema.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final UserMapper userMapper;
    final PasswordEncoder passwordEncoder;

    @SneakyThrows
    @Override
    public void register(RegisterUserRequest registerUserRequest) {
        User existedUer = userRepository.findByUserName(registerUserRequest.getEmail());
        if(existedUer != null) {
            throw new AppException(ErrorDetail.ERR_USER_EMAIL_EXISTED);
        }
        User user = userMapper.toUser(registerUserRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }
}
