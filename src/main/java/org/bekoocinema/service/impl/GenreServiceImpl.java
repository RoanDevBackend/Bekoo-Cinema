package org.bekoocinema.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.entity.Genre;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.repository.GenreRepository;
import org.bekoocinema.service.GenreService;
import org.springframework.stereotype.Service;

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
    public void deleteGenre(String genreId) {
        genreRepository.deleteById(genreId);
    }
}
