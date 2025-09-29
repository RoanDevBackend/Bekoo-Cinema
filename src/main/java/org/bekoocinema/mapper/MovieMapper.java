package org.bekoocinema.mapper;

import org.bekoocinema.entity.Genre;
import org.bekoocinema.entity.Movie;
import org.bekoocinema.request.movie.CreateMovieRequest;
import org.bekoocinema.response.movie.MovieResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class MovieMapper {

    @Mapping(target = "releaseDate", expression = "java(this.convertDate(createMovieRequest.getReleaseDate()))")
    @Mapping(target = "closeDate", expression = "java(this.convertDate(createMovieRequest.getCloseDate()))")
    public abstract Movie toMovie(CreateMovieRequest createMovieRequest);
    protected LocalDateTime convertDate(String dateTimeString){
        return LocalDateTime.parse(dateTimeString);
    }

    @Mapping(target = "genres", expression = "java(this.convertGenre(movie.getGenres()))")
    public abstract MovieResponse toMovieResponse(Movie movie);
    protected List<String> convertGenre(Set<Genre> genres){
        return genres.stream().map(Genre::getName).toList();
    }

}
