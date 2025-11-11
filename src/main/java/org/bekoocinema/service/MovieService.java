package org.bekoocinema.service;

import org.bekoocinema.request.movie.CreateMovieRequest;
import org.bekoocinema.request.movie.UpdateMovieRequest;
import org.bekoocinema.response.PageResponse;
import org.bekoocinema.response.movie.MovieResponse;

import java.time.LocalDate;
import java.util.List;

public interface MovieService {
    void addMovie(CreateMovieRequest createMovieRequest);
    void updateMovie(String movieId, UpdateMovieRequest updateMovieRequest);
    void deleteMovie(String movieId);
    PageResponse<?> filterMovie(String searchName, String genre, int minPrice, int maxPrice, int pageIndex, int pageSize, String orderType, String sortDirection);
    PageResponse<?> getMovieShowing(int pageIndex, int pageSize);
    PageResponse<?> getUpcomingMovie(int pageIndex, int pageSize);
    MovieResponse getMovieById(String id);

    List<MovieResponse> getMovieByDate(String date);
}
