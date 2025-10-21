package org.bekoocinema.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.entity.User;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.mapper.UserMapper;
import org.bekoocinema.repository.UserRepository;
import org.bekoocinema.request.auth.RegisterUserRequest;
import org.bekoocinema.response.PageResponse;
import org.bekoocinema.response.user.UserResponse;
import org.bekoocinema.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public PageResponse<UserResponse> getAllUsers(String key, int pageIndex, int pageSize) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<User> userPage = userRepository.findAllWithSearch(key, pageable);

        List<UserResponse> userResponses = userPage.getContent()
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
                .pageIndex(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .sortBy(new PageResponse.SortBy("createdAt", "DESC"))
                .content(userResponses)
                .build();
    }
}
