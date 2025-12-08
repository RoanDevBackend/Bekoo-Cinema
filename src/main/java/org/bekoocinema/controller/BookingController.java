package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.constant.EndPointConstant;
import org.bekoocinema.entity.User;
import org.bekoocinema.request.booking.BookingRequest;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.BookingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookingController {
    final BookingService bookingService;

    @Operation(
            summary = "User tạo đơn",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @PostMapping("/booking")
    public ApiResponse booking(@RequestBody @Valid BookingRequest bookingRequest, @AuthenticationPrincipal User user, HttpServletRequest request) {
        return ApiResponse.success(200, "Hãy thanh toán", bookingService.booking(bookingRequest, user, request));
    }

    @Operation(
            summary = "Lấy đơn của user",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @GetMapping(EndPointConstant.PUBLIC + "/booking")
    public ApiResponse getBooking(@AuthenticationPrincipal User user, @RequestParam(required = false, defaultValue = "") String bookingId){
        return ApiResponse.success(200, "Thành công", bookingService.getBookings(user, bookingId));
    }
}
