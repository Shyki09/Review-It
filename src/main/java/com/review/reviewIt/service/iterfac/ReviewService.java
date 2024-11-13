package com.review.reviewIt.service.iterfac;

import com.review.reviewIt.entity.Response;
import com.review.reviewIt.entity.User;

public interface ReviewService {

    Response addReview(Long restaurantId , Long rating , String reviewText , User authenticatedUser);

    Response getAllReview();


    Response getReviewById(Long reviewId);

    Response updateReview(Long reviewId , Long rating , String reviewText , User authenticatedUser);

    Response deleteReview(Long reviewId ,  User authenticatedUser);
}