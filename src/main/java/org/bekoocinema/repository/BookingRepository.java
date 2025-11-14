package org.bekoocinema.repository;

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
}
