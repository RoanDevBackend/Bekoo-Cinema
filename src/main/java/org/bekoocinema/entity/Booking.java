package org.bekoocinema.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    LocalDateTime bookingDate;
    String movieName;
    String movieId;
    String genreName;
    String seatNames;
    String seatIds;
    String roomName;
    String cinemaName;
    String cinemaAddress;
    String paymentStatus;
    LocalDateTime paymentDate;
    LocalDateTime checkInDate;
    String userId;
    String fullName;
    String email;
    String phone;
    long totalPrice;
}
