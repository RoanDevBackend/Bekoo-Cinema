package org.bekoocinema.controller;

import lombok.RequiredArgsConstructor;
import org.bekoocinema.constant.EndPointConstant;
import org.bekoocinema.entity.User;
import org.bekoocinema.request.comment.NewCommentRequest;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    final CommentService commentService;

    @PostMapping("/comment")
    public ApiResponse newComment(@RequestBody NewCommentRequest newCommentRequest, @AuthenticationPrincipal User user) {
        commentService.newComment(newCommentRequest, user);
        return ApiResponse.success(201, "Thêm bình luận thành công");
    }

    @GetMapping(EndPointConstant.PUBLIC + "/comment/{movieId}")
    public ApiResponse getCommentByMovie(@PathVariable String movieId) {
        return ApiResponse.success(200, "Danh sách comment theo phim", commentService.getCommentByMovie(movieId));
    }

    @GetMapping(EndPointConstant.PUBLIC + "/comment/parent/{parentCommentId}")
    public ApiResponse getCommentByParentComment(@PathVariable String parentCommentId) {
        return ApiResponse.success(200, "", commentService.getCommentByParent(parentCommentId));
    }
}
