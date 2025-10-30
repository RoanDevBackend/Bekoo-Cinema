package org.bekoocinema.response.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {
    String id;
    LocalDateTime bookingDate;
    String movieName;
    String movieId;
    String genreName;
    String seats;
    String roomName;
    String cinemaName;
    String cinemaAddress;
    String paymentMethod;
    String paymentStatus;
    LocalDateTime paymentDate;
    LocalDateTime checkInDate;
    String userId;
    String fullName;
    String email;
    String phone;
    long totalPrice;
}
