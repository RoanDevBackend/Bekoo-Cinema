package org.bekoocinema.repository;

import org.bekoocinema.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, String> {
}
