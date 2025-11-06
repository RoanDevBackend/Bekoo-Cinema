package org.bekoocinema.repository;

import org.bekoocinema.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, String> {

    @Query("""
        SELECT DISTINCT m
        FROM Movie m
        LEFT JOIN FETCH m.showtimes s
        LEFT JOIN FETCH m.genres g
        WHERE s.startTime BETWEEN :startOfDay AND :endOfDay
    """)
    List<Movie> getMovieByDate(@Param("startOfDay") LocalDateTime startOfDay,
                               @Param("endOfDay") LocalDateTime endOfDay);

}
