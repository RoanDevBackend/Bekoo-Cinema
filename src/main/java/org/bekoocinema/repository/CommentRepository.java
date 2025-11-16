package org.bekoocinema.repository;

import org.bekoocinema.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    @Query("FROM Comment c " +
            "WHERE c.movie.id = :movieId " +
            "AND c.parent IS NULL ")
    List<Comment> getCommentByMovie(String movieId);

    @Query("FROM Comment c " +
            "WHERE c.parent.id = :parentCommentId ")
    List<Comment> getCommentByParentComment(String parentCommentId);
}
