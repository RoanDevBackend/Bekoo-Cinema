package org.bekoocinema.repository;

import org.bekoocinema.entity.Room;
import org.bekoocinema.response.room.RoomSimpleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, String> {
    @Query("FROM Room r " +
            "WHERE r.cinema.id = :cinemaId ")
    List<Room> getRoomByCinema(String cinemaId);

    @Query("""
        SELECT new org.bekoocinema.response.room.RoomSimpleResponse(r.id, r.name)
        FROM Room r
        WHERE r.cinema.id = :cinemaId
    """)
    List<RoomSimpleResponse> getRoomByCinemaSimple(@Param("cinemaId") String cinemaId);
}
