package org.bekoocinema.service;

import jakarta.servlet.http.HttpServletRequest;
import org.bekoocinema.entity.User;
import org.bekoocinema.request.booking.BookingRequest;
import org.bekoocinema.response.booking.BookingResponse;

import java.util.List;
import java.util.Map;

public interface BookingService {
    String booking(BookingRequest bookingRequest, User user, HttpServletRequest request);
    List<BookingResponse> getBookings(User user);
    String executePaymentResult(Map<String, String> params, HttpServletRequest request);
}
