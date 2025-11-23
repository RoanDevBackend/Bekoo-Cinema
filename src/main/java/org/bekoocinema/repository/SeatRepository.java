package org.bekoocinema.repository;

import jakarta.transaction.Transactional;
import org.bekoocinema.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, String> {
    @Query("FROM Seat s " +
            "WHERE s.id in :ids ")
    List<Seat> getSeatInId(List<String> ids);
}
