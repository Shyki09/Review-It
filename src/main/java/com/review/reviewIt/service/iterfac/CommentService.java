package com.review.reviewIt.service.iterfac;

import com.review.reviewIt.entity.Response;
import com.review.reviewIt.entity.User;


public interface CommentService {

    Response addComment(Long reviewId, String text , User authenticatedUser);

    Response updateComment(Long commentId, String text ,  User authenticatedUser);

    Response deleteComment(Long commentId ,  User authenticatedUser);

    Response getCommentById(Long commentId);

    Response getCommentsByReview(Long reviewId);

    Response getCommentsByUser(Long userId);

}
