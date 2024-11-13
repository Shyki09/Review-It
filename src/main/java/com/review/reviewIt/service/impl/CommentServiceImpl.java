package com.review.reviewIt.service.impl;


import com.review.reviewIt.dto.CommentDTO;
import com.review.reviewIt.entity.Response;
import com.review.reviewIt.entity.Comment;
import com.review.reviewIt.entity.Review;
import com.review.reviewIt.entity.User;
import com.review.reviewIt.exception.CustomException;
import com.review.reviewIt.repository.CommentRepository;
import com.review.reviewIt.repository.ReviewRepository;
import com.review.reviewIt.repository.UserRepository;
import com.review.reviewIt.service.iterfac.CommentService;
import com.review.reviewIt.utils.JWTUtils;
import com.review.reviewIt.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Autowired
    private JWTUtils jwtUtils;

    public CommentServiceImpl(CommentRepository commentRepository, ReviewRepository reviewRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public Response addComment(Long reviewId, String text , User authenticatedUser) {
        Response response = new Response();
        try {
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new CustomException("Review not found"));

            if (text == null || text.trim().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Comment text cannot be empty.");
                return response;
            }

            // Create and save the comment
            Comment comment = new Comment();
            comment.setUser(authenticatedUser);
            comment.setReview(review);
            comment.setText(text);

            commentRepository.save(comment);

            // Set success response
            response.setStatusCode(201);
            response.setMessage("Comment added successfully.");
            response.setComment(Utils.mapCommentEntityToCommentDTO(comment));

        } catch (CustomException e) {
            // Set error response for custom exceptions
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            // Set error response for general exceptions
            response.setStatusCode(500);
            response.setMessage("Error adding comment: " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response updateComment(Long commentId, String text , User authenticatedUser) {
        Response response = new Response();
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException("Comment not Found"));

            if(!comment.getUser().equals(authenticatedUser) && !authenticatedUser.getRoles().contains("ADMIN")) {
                response.setStatusCode(403); // Forbidden
                response.setMessage("You are not authorized to update this comment.");
                return response;
            }

            if (text != null) comment.setText(text);
            Comment updatecomment = commentRepository.save(comment);
            CommentDTO commentDTO = Utils.mapCommentEntityToCommentDTO(updatecomment);

            response.setStatusCode(200);
            response.setMessage("Comment Updated successfully.");
            response.setComment(commentDTO);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Updating comment: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteComment(Long commentId , User authenticatedUser) {
        Response response = new Response();
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException("Comment not Found"));

            if(!comment.getUser().equals(authenticatedUser)) {
                response.setStatusCode(403); // Forbidden
                response.setMessage("You are not authorized to update this comment.");
                return response;
            }

            commentRepository.deleteById(commentId);
            response.setStatusCode(200);
            response.setMessage("Comment Deleted Successfully.");

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Deleting the Comment." + e.getMessage());
        }
        return response;
    }


    @Override
    public Response getCommentById(Long commentId) {
        Response response = new Response();
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException("Comment not Found"));
            ;

            CommentDTO commentDTO = Utils.mapCommentEntityToCommentDTO(comment);

            response.setStatusCode(200);
            response.setMessage("Comment retrieved successfully.");
            response.setComment(commentDTO);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Getting the Comment." + e.getMessage());
        }
        return response;
    }


    @Override
    public Response getCommentsByReview(Long reviewId) {
        Response response = new Response();
        try {
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new CustomException("Review not Found"));
            ;

            if (review.getComments() == null || review.getComments().isEmpty()) {
                response.setStatusCode(200); // No Content
                response.setMessage("Review has no comments.");
                return response;
            }
            List<CommentDTO> commentDTOList = Utils.mapCommentListEntityToCommentListDTO(review.getComments());

            response.setStatusCode(200);
            response.setMessage("Comments retrieved successfully.");
            response.setCommentList(commentDTOList);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving Comment." + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getCommentsByUser(Long userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException("User not Found"));


            if (user.getComments() == null || user.getComments().isEmpty()) {
                response.setStatusCode(204);
                response.setMessage("User has no comments.");
                return response;
            }
            List<CommentDTO> commentDTOList = Utils.mapCommentListEntityToCommentListDTO(user.getComments());

            response.setStatusCode(200);
            response.setMessage("Comments retrieved successfully.");
            response.setCommentList(commentDTOList);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving Comment." + e.getMessage());
        }
        return response;
    }
}
