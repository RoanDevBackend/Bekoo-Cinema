package org.bekoocinema.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.entity.User;
import org.bekoocinema.request.booking.BookingRequest;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.BookingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookingController {
    final BookingService bookingService;

    @PostMapping("/booking")
    public ApiResponse booking(@RequestBody @Valid BookingRequest bookingRequest, @AuthenticationPrincipal User user, HttpServletRequest request) {
        return ApiResponse.success(200, "Hãy thanh toán", bookingService.booking(bookingRequest, user, request));
    }

    @GetMapping("/booking")
    public ApiResponse getBooking(@AuthenticationPrincipal User user) {
        return ApiResponse.success(200, "Thành công",bookingService.getBookings(user));
    }
}
