package org.bekoocinema.repository;

import org.bekoocinema.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, String> {
    @Query("FROM Showtime st " +
            "WHERE st.movie.id = :movieId " +
            "AND st.room.cinema.id = :cinemaId ")
    List<Showtime> getShowtime(String movieId, String cinemaId);

    @Query("SELECT COUNT(st) > 0 FROM Showtime st " +
            "WHERE st.room.id = :roomId " +
            "AND ((st.startTime < :endTime AND st.endTime > :startTime))")
    boolean existsConflictingShowtime(
            @Param("roomId") String roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
