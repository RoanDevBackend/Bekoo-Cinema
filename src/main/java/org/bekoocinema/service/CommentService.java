package org.bekoocinema.service;

import org.bekoocinema.entity.User;
import org.bekoocinema.request.comment.NewCommentRequest;
import org.bekoocinema.response.comment.CommentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    void newComment(NewCommentRequest newCommentRequest, User user);
    List<CommentResponse> getCommentByMovie(String movieId);
    List<CommentResponse> getCommentByParent(String parentCommentId);
}
