package com.review.reviewIt.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.review.reviewIt.dto.CommentDTO;
import com.review.reviewIt.dto.RestaurantDTO;
import com.review.reviewIt.dto.ReviewDTO;
import com.review.reviewIt.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private Long id;
    private int statusCode;
    private String message;
    private String token;
    private String expirationTime;
    private String role;

    // Entities
    private UserDTO user;
    private RestaurantDTO restaurant;
    private ReviewDTO review;
    private CommentDTO comment;

    // Lists of DTOs for bulk data responses
    private List<UserDTO> userList;
    private List<RestaurantDTO> restaurantList;
    private List<ReviewDTO> reviewList;
    private List<CommentDTO> commentList;

}
