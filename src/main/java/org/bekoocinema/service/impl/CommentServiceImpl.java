package org.bekoocinema.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.entity.Comment;
import org.bekoocinema.entity.Movie;
import org.bekoocinema.entity.User;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.repository.CommentRepository;
import org.bekoocinema.repository.MovieRepository;
import org.bekoocinema.request.comment.NewCommentRequest;
import org.bekoocinema.response.comment.CommentResponse;
import org.bekoocinema.service.CommentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    final CommentRepository commentRepository;
    final MovieRepository movieRepository;

    @Override
    @SneakyThrows
    public void newComment(NewCommentRequest newCommentRequest, User user) {
        Comment comment = new Comment();
        comment.setContent(newCommentRequest.getContent());
        comment.setUser(user);
        Movie movie = movieRepository.findById(newCommentRequest.getMovieId())
                .orElseThrow(() -> new AppException(ErrorDetail.ERR_MOVIE_NOT_EXISTED));
        comment.setMovie(movie);
        if(!newCommentRequest.getParentCommentId().isBlank()){
            Comment commentParent = commentRepository.findById(newCommentRequest.getParentCommentId())
                    .orElseThrow(() -> new AppException(ErrorDetail.ERR_COMMENT_PARENT_NOT_EXISTED));
            comment.setParent(commentParent);
        }
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public List<CommentResponse> getCommentByMovie(String movieId) {
        return commentRepository.getCommentByMovie(movieId).stream().map(t -> {
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setId(t.getId());
            commentResponse.setContent(t.getContent());
            commentResponse.setAuthor(t.getUser().getFullName());
            commentResponse.setTotalChildComment(commentRepository.getCommentByParentComment(t.getId()).size());
            return commentResponse;
        }).toList();
    }

    @Override
    @Transactional
    public List<CommentResponse> getCommentByParent(String parentCommentId) {
        return commentRepository.getCommentByParentComment(parentCommentId).stream()
                .map(t -> {
                    CommentResponse commentResponse = new CommentResponse();
                    commentResponse.setId(t.getId());
                    commentResponse.setContent(t.getContent());
                    commentResponse.setAuthor(t.getUser().getFullName());
                    commentResponse.setTotalChildComment(commentRepository.getCommentByParentComment(t.getId()).size());
                    return commentResponse;
                })
                .toList();
    }
}
