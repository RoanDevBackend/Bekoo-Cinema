package org.bekoocinema.repository;

import org.bekoocinema.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

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

    @Query("""
        SELECT DISTINCT m
        FROM Movie m
        LEFT JOIN FETCH m.showtimes s
        LEFT JOIN FETCH m.genres g
        WHERE s.room.cinema.id = :cinemaId
        AND s.startTime BETWEEN :startOfDay AND :endOfDay
    """)
    List<Movie> getMoviesByCinemaAndDate(@Param("cinemaId") String cinemaId,
                                         @Param("startOfDay") LocalDateTime startOfDay,
                                         @Param("endOfDay") LocalDateTime endOfDay);

    @Query("""
        FROM Movie m
        WHERE m.releaseDate < :now
        AND m.closeDate > :now
    """)
    Page<Movie> findCurrentlyShowing(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("""
        FROM Movie m
        WHERE m.releaseDate BETWEEN :now AND :fiveHoursLater
    """)
    Page<Movie> findUpcomingMovies(@Param("now") LocalDateTime now,
                                   @Param("fiveHoursLater") LocalDateTime fiveHoursLater,
                                   Pageable pageable);


}
