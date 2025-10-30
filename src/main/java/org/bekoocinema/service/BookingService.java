package org.bekoocinema.service;

import org.bekoocinema.entity.User;
import org.bekoocinema.request.booking.BookingRequest;
import org.bekoocinema.response.booking.BookingResponse;

import java.util.List;

public interface BookingService {
    void booking(BookingRequest bookingRequest, User user);
    List<BookingResponse> getBookings(User user);
}
