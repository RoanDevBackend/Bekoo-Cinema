package org.bekoocinema.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.bekoocinema.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByUserId(String userId);

    @Query(
        "FROM Booking b " +
            "WHERE b.userId = :userId " +
            "AND (:id IS NULL OR :id = '' OR b.id LIKE CONCAT('%', :id, '%'))"
    )
    List<Booking> findAllByUserIdAndId(String userId, String id);

    long countByPaymentStatus(String paymentStatus);

    @Query(
        "SELECT SUM(b.totalPrice) FROM Booking b WHERE b.paymentStatus = :paymentStatus"
    )
    Long sumTotalPriceByPaymentStatus(String paymentStatus);

    @Query("SELECT b.seatIds " +
            "FROM Booking b " +
            "WHERE b.showtimeId = :showtimeId ")
    List<String> getSeatBooked(String showtimeId);

    @Query("FROM Booking b " +
            "WHERE b.paymentDate IS NULL " +
            "AND b.bookingDate < :cutOff ")
    List<Booking> getBookingNotPayment(LocalDateTime cutOff);

    boolean existsByShowtimeId(String showtimeId);

}
