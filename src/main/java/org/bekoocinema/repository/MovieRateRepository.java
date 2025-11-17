package org.bekoocinema.repository;

import org.bekoocinema.entity.MovieRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRateRepository extends JpaRepository<MovieRate, String> {
    @Query("FROM MovieRate mv " +
            "WHERE mv.movie.id = :movieId " +
            "AND mv.user.id = :userId ")
    Optional<MovieRate> findByMovieIdAndUserId(String movieId, String userId);

    @Query("SELECT mv.rating FROM MovieRate mv WHERE mv.movie.id = :movieId")
    List<Integer> getByMovie(String movieId);
}
