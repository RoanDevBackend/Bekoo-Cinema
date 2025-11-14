package org.bekoocinema.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.bekoocinema.entity.User;
import org.bekoocinema.request.booking.BookingRequest;
import org.bekoocinema.response.booking.BookingResponse;

public interface BookingService {
    String booking(BookingRequest bookingRequest, User user, HttpServletRequest request);
    List<BookingResponse> getBookings(User user, String bookingId);
    String executePaymentResult(Map<String, String> params, HttpServletRequest request);
}
