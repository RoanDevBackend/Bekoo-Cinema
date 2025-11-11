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

    @Query(
        "SELECT DISTINCT st FROM Showtime st " +
            "LEFT JOIN FETCH st.movie m " +
            "LEFT JOIN FETCH m.genres " +
            "LEFT JOIN FETCH st.room r " +
            "LEFT JOIN FETCH r.cinema " +
            "WHERE r.cinema.id = :cinemaId " +
            "AND st.startTime >= :startDateTime " +
            "AND st.startTime <= :endDateTime " +
            "ORDER BY st.startTime ASC"
    )
    List<Showtime> findByCinemaAndDateRange(
        @Param("cinemaId") String cinemaId,
        @Param("startDateTime") LocalDateTime startDateTime,
        @Param("endDateTime") LocalDateTime endDateTime
    );
}
