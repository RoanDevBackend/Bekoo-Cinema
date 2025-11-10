package org.bekoocinema.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.entity.Genre;
import org.bekoocinema.entity.Movie;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.repository.GenreRepository;
import org.bekoocinema.response.GenreResponse.GenreResponse;
import org.bekoocinema.service.GenreService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    final GenreRepository genreRepository;

    @SneakyThrows
    @Override
    public void addGenre(String genreName) {
        if(genreRepository.existsByName(genreName)) {
            throw new AppException(ErrorDetail.ERR_GENRE_EXISTED);
        }
        Genre genre = new Genre();
        genre.setName(genreName);
        genreRepository.save(genre);
    }

    @Override
    @SneakyThrows
    @Transactional
    public void deleteGenre(String genreId) {
        Genre genre = genreRepository.findById(genreId).orElseThrow(
                () -> new AppException(ErrorDetail.ERR_GENRE_NOT_EXISTED)
        );
        for (Movie movie : genre.getMovies()) {
            movie.getGenres().remove(genre);
        }
        genre.getMovies().clear();
        genreRepository.delete(genre);
    }

    @Override
    public List<GenreResponse> getAllGenres(String name) {
        List<Genre> genres = genreRepository.findAllByName(name);
        List<GenreResponse> genreResponses = new ArrayList<>();
        for(Genre genre : genres) {
            GenreResponse genreResponse = new GenreResponse();
            genreResponse.setId(genre.getId());
            genreResponse.setName(genre.getName());
            genreResponses.add(genreResponse);
        }
        return genreResponses;
    }
}
