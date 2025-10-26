package org.bekoocinema.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.entity.Status;
import org.bekoocinema.entity.User;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.mapper.UserMapper;
import org.bekoocinema.repository.UserRepository;
import org.bekoocinema.request.auth.RegisterUserRequest;
import org.bekoocinema.request.auth.UpdateUserRequest;
import org.bekoocinema.response.PageResponse;
import org.bekoocinema.response.user.UserResponse;
import org.bekoocinema.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Override
    public UserResponse update(String id, UpdateUserRequest updateUserRequest)
        throws AppException {
        Optional<User> existedUser = userRepository.findById(id);
        if (existedUser.isEmpty()) {
            throw new AppException(ErrorDetail.ERR_USER_NOT_EXISTED);
        }

        User user = existedUser.get();

        if (updateUserRequest.getFirstName() != null) {
            user.setFirstName(updateUserRequest.getFirstName());
        }
        if (updateUserRequest.getLastName() != null) {
            user.setLastName(updateUserRequest.getLastName());
        }
        if (updateUserRequest.getPhone() != null) {
            user.setPhone(updateUserRequest.getPhone());
        }
        if (updateUserRequest.getRole() != null) {
            user.setRole(updateUserRequest.getRole());
        }
        if (updateUserRequest.getStatus() != null) {
            user.setStatus(updateUserRequest.getStatus());
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public void delete(String id) throws AppException {
        String currentUserId = getCurrentUserId();

        if (id.equals(currentUserId)) {
            throw new AppException(ErrorDetail.ERR_USER_CANNOT_DELETE_SELF);
        }

        Optional<User> existedUser = userRepository.findById(id);
        if (existedUser.isEmpty()) {
            throw new AppException(ErrorDetail.ERR_USER_NOT_EXISTED);
        }

        User user = existedUser.get();
        user.setStatus(Status.INACTIVE);
        userRepository.save(user);
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
            authentication.getPrincipal() instanceof UserDetails userDetails) {
            if (userDetails instanceof User user) {
                return user.getId();
            }
        }

        return null;
    }
}
