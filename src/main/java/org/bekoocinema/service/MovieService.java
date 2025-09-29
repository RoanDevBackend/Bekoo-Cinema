package org.bekoocinema.service;

import org.bekoocinema.request.movie.CreateMovieRequest;
import org.bekoocinema.response.PageResponse;

public interface MovieService {
    void addMovie(CreateMovieRequest createMovieRequest);
    PageResponse<?> filterMovie(String searchName, String genre, int minPrice, int maxPrice, int pageIndex, int pageSize, String orderType, String sortDirection);
}
