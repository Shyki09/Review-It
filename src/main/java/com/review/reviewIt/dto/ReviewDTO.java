package com.review.reviewIt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.review.reviewIt.entity.Comment;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDTO {
    private Long id;
    private Long rating;
    private String reviewText;
    private UserDTO user;
    private RestaurantDTO restaurant;
    private List<CommentDTO> comments = new ArrayList<>();


}
