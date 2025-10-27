package org.bekoocinema.repository;

import org.bekoocinema.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, String> {
    @Query("FROM Room r " +
            "WHERE r.cinema.id = :cinemaId ")
    List<Room> getRoomByCinema(String cinemaId);
}
