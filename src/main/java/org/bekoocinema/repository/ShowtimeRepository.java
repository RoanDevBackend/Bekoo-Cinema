package org.bekoocinema.repository;

import org.bekoocinema.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, String> {
    @Query("FROM Showtime st " +
            "WHERE st.movie.id = :movieId " +
            "AND st.room.cinema.id = :cinemaId ")
    List<Showtime> getShowtime(String movieId, String cinemaId);
}
