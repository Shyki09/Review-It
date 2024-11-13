package com.review.reviewIt.utils;

import com.review.reviewIt.dto.CommentDTO;
import com.review.reviewIt.dto.RestaurantDTO;
import com.review.reviewIt.dto.ReviewDTO;
import com.review.reviewIt.dto.UserDTO;
import com.review.reviewIt.entity.Comment;
import com.review.reviewIt.entity.Restaurant;
import com.review.reviewIt.entity.Review;
import com.review.reviewIt.entity.User;
import com.review.reviewIt.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
        return userDTO;
    }

    public static RestaurantDTO mapRestaurantEntityToRestaurantDTO(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setId(restaurant.getId());
        restaurantDTO.setName(restaurant.getName());
        restaurantDTO.setCuisine(restaurant.getCuisine());
        restaurantDTO.setAddress(restaurant.getAddress());
        restaurantDTO.setRestaurantDescription(restaurant.getRestaurantDescription());
        return restaurantDTO;
    }

    public static ReviewDTO mapReviewEntityToReviewDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setReviewText(review.getReviewText());
        reviewDTO.setRating(review.getRating());
        return reviewDTO;
    }

    public static CommentDTO mapCommentEntityToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setText(comment.getText());
        return commentDTO;
    }

    public static UserDTO mapUserEntityToUserDTOPlusReviews(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles().stream()
                .map(Role::getName) // Assuming Role has a getName() method
                .collect(Collectors.toList()));

        if (!user.getReviews().isEmpty()) {
            userDTO.setReviews(
                    user.getReviews().stream()
                            .map(Utils::mapReviewEntityToReviewDTOPlusComments)
                            .collect(Collectors.toList()));
        }
        return userDTO;
    }





    public static RestaurantDTO mapRestaurantEntityToRestaurantDTOPlusReviews(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setId(restaurant.getId());
        restaurantDTO.setName(restaurant.getName());
        restaurantDTO.setCuisine(restaurant.getCuisine());
        restaurantDTO.setAddress(restaurant.getAddress());
        restaurantDTO.setRestaurantDescription(restaurantDTO.getRestaurantDescription());

        if (!restaurant.getReviews().isEmpty()) {
            restaurantDTO.setReviews(
                    restaurant.getReviews().stream()
                            .map(Utils::mapReviewEntityToReviewDTOPlusComments)
                            .collect(Collectors.toList())
            );
        }
        return restaurantDTO;
    }

    public static ReviewDTO mapReviewEntityToReviewDTOPlusComments(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setReviewText(review.getReviewText());
        reviewDTO.setRating(review.getRating());

        if (!review.getComments().isEmpty()) {
            reviewDTO.setComments(
                    review.getComments().stream()
                            .map(Utils::mapCommentEntityToCommentDTO)
                            .collect(Collectors.toList())
            );
        }
        return reviewDTO;
    }

    public static CommentDTO mapCommentEntityToCommentDTOPlusUser(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setText(comment.getText());

        if (comment.getUser() != null) {
            commentDTO.setUser(mapUserEntityToUserDTO(comment.getUser()));
        }

        return commentDTO;
    }

    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    public static List<RestaurantDTO> mapRestaurantListEntityToRestaurantListDTO(List<Restaurant> restaurantList) {
        return restaurantList.stream().map(Utils::mapRestaurantEntityToRestaurantDTO).collect(Collectors.toList());
    }

    public static List<ReviewDTO> mapReviewListEntityToReviewListDTO(List<Review> reviewList) {
        return reviewList.stream().map(Utils::mapReviewEntityToReviewDTO).collect(Collectors.toList());
    }

    public static List<CommentDTO> mapCommentListEntityToCommentListDTO(List<Comment> commentList) {
        return commentList.stream().map(Utils::mapCommentEntityToCommentDTO).collect(Collectors.toList());
    }

}
