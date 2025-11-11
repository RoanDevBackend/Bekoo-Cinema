package org.bekoocinema.service;

import org.bekoocinema.response.GenreResponse.GenreResponse;

import java.util.List;

public interface GenreService {
    void addGenre(String genreName);
    void updateGenre(String genreId, String newName);
    void deleteGenre(String genreId);
    List<GenreResponse> getAllGenres(String name);
}
