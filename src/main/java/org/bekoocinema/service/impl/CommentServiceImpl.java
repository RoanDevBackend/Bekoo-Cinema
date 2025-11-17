package org.bekoocinema.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.entity.Comment;
import org.bekoocinema.entity.Movie;
import org.bekoocinema.entity.MovieRate;
import org.bekoocinema.entity.User;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.repository.CommentRepository;
import org.bekoocinema.repository.MovieRateRepository;
import org.bekoocinema.repository.MovieRepository;
import org.bekoocinema.request.comment.NewCommentRequest;
import org.bekoocinema.request.comment.NewRatingRequest;
import org.bekoocinema.response.comment.CommentResponse;
import org.bekoocinema.service.CommentService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    final CommentRepository commentRepository;
    final MovieRepository movieRepository;
    final MovieRateRepository movieRateRepository;

    @Override
    @SneakyThrows
    public void newComment(NewCommentRequest newCommentRequest, User user) {
        Comment comment = new Comment();
        comment.setContent(newCommentRequest.getContent());
        comment.setUser(user);
        Movie movie = movieRepository.findById(newCommentRequest.getMovieId())
                .orElseThrow(() -> new AppException(ErrorDetail.ERR_MOVIE_NOT_EXISTED));
        comment.setMovie(movie);
        if(newCommentRequest.getParentCommentId() != null && !newCommentRequest.getParentCommentId().isBlank()){
            Comment commentParent = commentRepository.findById(newCommentRequest.getParentCommentId())
                    .orElseThrow(() -> new AppException(ErrorDetail.ERR_COMMENT_PARENT_NOT_EXISTED));
            comment.setParent(commentParent);
        }
        commentRepository.save(comment);
    }

    @Override
    @SneakyThrows
    public String newRate(NewRatingRequest newRatingRequest, User user) {
        Movie movie = movieRepository.findById(newRatingRequest.getMovieId())
                .orElseThrow(() -> new AppException(ErrorDetail.ERR_MOVIE_NOT_EXISTED));
        Optional<MovieRate> movieRateExisted = movieRateRepository.findByMovieIdAndUserId(movie.getId(), user.getId());
        if(movieRateExisted.isPresent()){
            MovieRate movieRate = movieRateExisted.get();
            movieRate.setRating(newRatingRequest.getRate());
            movieRateRepository.save(movieRate);
            return "Đã update rating";
        }
        MovieRate movieRate = new MovieRate();
        movieRate.setRating(newRatingRequest.getRate());
        movieRate.setUser(user);
        movieRate.setMovie(movie);
        movieRateRepository.save(movieRate);
        return "Đã thêm lượt rating";
    }

    @Override
    @Transactional
    public List<CommentResponse> getCommentByMovie(String movieId) {
        return commentRepository.getCommentByMovie(movieId).stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<CommentResponse> getCommentByParent(String parentCommentId) {
        return commentRepository.getCommentByParentComment(parentCommentId).stream()
                .map(this::convertToResponse)
                .toList();
    }

    private CommentResponse convertToResponse(Comment t) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(t.getId());
        commentResponse.setContent(t.getContent());
        commentResponse.setAuthor(t.getUser().getFullName());
        commentResponse.setCreatedDate(this.convertDateToString(t.getCreatedAt()));
        commentResponse.setTotalChildComment(commentRepository.getCommentByParentComment(t.getId()).size());
        return commentResponse;
    }

    private String convertDateToString(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(time, now);

        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (seconds < 60) {
            return "Vài giây trước";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else if (days <= 5) {
            return days + " ngày trước";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return time.format(formatter);
        }
    }
}
