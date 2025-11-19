package org.bekoocinema.mapper;

import lombok.RequiredArgsConstructor;
import org.bekoocinema.entity.Genre;
import org.bekoocinema.entity.Movie;
import org.bekoocinema.entity.Showtime;
import org.bekoocinema.repository.CommentRepository;
import org.bekoocinema.repository.MovieRateRepository;
import org.bekoocinema.request.movie.CreateMovieRequest;
import org.bekoocinema.response.comment.RateResponse;
import org.bekoocinema.response.movie.MovieResponse;
import org.bekoocinema.response.showtime.ShowtimeDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", imports = {java.time.LocalDateTime.class})
public abstract class MovieMapper {

    @Autowired
    MovieRateRepository movieRateRepository;
    @Autowired
    CommentRepository commentRepository;

    @Mapping(target = "releaseDate", expression = "java(this.convertDate(createMovieRequest.getReleaseDate()))")
    @Mapping(target = "closeDate", expression = "java(this.convertDate(createMovieRequest.getCloseDate()))")
    public abstract Movie toMovie(CreateMovieRequest createMovieRequest);
    protected LocalDateTime convertDate(String dateTimeString){
        return LocalDateTime.parse(dateTimeString);
    }

    @Mapping(target = "genres", expression = "java(this.convertGenre(movie.getGenres()))")
    @Mapping(target = "showtimeDetailResponses", expression = "java(this.convertShowtimes(movie.getShowtimes()))")
    @Mapping(target = "rate", expression = "java(this.getRating(movie.getId()))")
    @Mapping(target = "totalComment", expression = "java(this.getTotalComment(movie.getId()))")
    public abstract MovieResponse toMovieResponse(Movie movie);

    protected List<ShowtimeDetailResponse> convertShowtimes(Set<Showtime> showtimes) {
        if (showtimes == null || showtimes.isEmpty()) return List.of();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return showtimes.stream()
                .map(showtime -> {
                    ShowtimeDetailResponse response = new ShowtimeDetailResponse();
                    response.setId(showtime.getId());
                    response.setDate(showtime.getStartTime().toLocalDate().format(dateFormatter));
                    response.setStartTime(showtime.getStartTime().toLocalTime().format(timeFormatter));
                    response.setEndTime(showtime.getEndTime().toLocalTime().format(timeFormatter));
                    return response;
                })
                .toList();
    }

    protected RateResponse getRating(String movieId){
        List<Integer> ratings = movieRateRepository.getByMovie(movieId);
        IntSummaryStatistics stats = ratings.stream()
                .mapToInt(Integer::intValue)
                .summaryStatistics();

        double avgRating = stats.getAverage();
        long voteCount = stats.getCount();
        return new RateResponse(voteCount, avgRating);
    }

    protected long getTotalComment(String movieId){
        return commentRepository.totalCommentByMovie(movieId);
    }

    protected List<String> convertGenre(Set<Genre> genres){
        return genres.stream().map(Genre::getName).toList();
    }
}
