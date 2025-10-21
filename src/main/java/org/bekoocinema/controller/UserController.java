package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.response.PageResponse;
import org.bekoocinema.response.user.UserResponse;
import org.bekoocinema.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    final UserService userService;

    @Operation(summary = "Lấy tất cả user (only admin)", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestParam(required = false) String key,
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        PageResponse<UserResponse> users = userService.getAllUsers(key, pageIndex, pageSize);
        return ResponseEntity.ok(ApiResponse.success(200, "Get all users successfully", users));
    }
}
