package com.review.reviewIt.repository;

import com.review.reviewIt.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReviewId(Long reviewId);

    List<Comment> findByUserId(Long userId);
}
