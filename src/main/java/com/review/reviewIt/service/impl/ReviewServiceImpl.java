package com.review.reviewIt.service.impl;

import com.review.reviewIt.entity.Response;
import com.review.reviewIt.dto.ReviewDTO;
import com.review.reviewIt.entity.Restaurant;
import com.review.reviewIt.entity.Review;
import com.review.reviewIt.entity.User;
import com.review.reviewIt.exception.CustomException;
import com.review.reviewIt.repository.RestaurantRepository;
import com.review.reviewIt.repository.ReviewRepository;
import com.review.reviewIt.service.iterfac.ReviewService;
import com.review.reviewIt.utils.Utils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;


    private final RestaurantRepository restaurantRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Response addReview(Long restaurantId,
                              Long rating,
                              String reviewText ,
                              User authenticatedUser) {
        Response response = new Response();
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new CustomException("Restaurant not found"));

            Review review = new Review();
            review.setUser(authenticatedUser);
            review.setRestaurant(restaurant);
            review.setRating(rating);
            review.setReviewText(reviewText);

            reviewRepository.save(review);

            response.setStatusCode(201);
            response.setMessage("Review added successfully.");
            response.setReview(Utils.mapReviewEntityToReviewDTO(review));

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error adding review: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllReview() {
        Response response = new Response();
        try{
            List<Review> reviewList = reviewRepository.findAll(Sort.by(Sort.Direction.DESC , "rating"));
            List<ReviewDTO> reviewDTOList = Utils.mapReviewListEntityToReviewListDTO(reviewList);

            response.setStatusCode(200);
            response.setMessage("Successfully retrieved the Reviews");
            response.setReviewList(reviewDTOList);

        }catch (CustomException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());


        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Getting the Reviews." + e.getMessage());
        }
        return  response;
    }


    @Override
    public Response getReviewById(Long reviewId) {
        Response response = new Response();
        try{
           Review review = reviewRepository.findById(reviewId)
                   .orElseThrow(() -> new CustomException("Review Id Not Found"));

           ReviewDTO reviewDTO = Utils.mapReviewEntityToReviewDTO(review);

            response.setStatusCode(200);
            response.setMessage("Review retrieved successfully.");
            response.setReview(reviewDTO);

        }catch (CustomException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());


        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Getting the Review." + e.getMessage());
        }
        return  response;
    }

    @Override
    public Response updateReview(Long reviewId, Long rating,String reviewText , User authenticatedUser) {
        Response response = new Response();
        try{
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new CustomException("Review Id Not Found"));

            if(!review.getUser().equals(authenticatedUser) && !authenticatedUser.getRoles().contains("ADMIN")) {
                response.setStatusCode(403); // Forbidden
                response.setMessage("You are not authorized to update this review.");
                return response;
            }

            if(rating != null) review.setRating(rating);
            if(reviewText != null) review.setReviewText(reviewText);

            Review updateReview = reviewRepository.save(review);
            ReviewDTO reviewDTO = Utils.mapReviewEntityToReviewDTO(updateReview);

            response.setStatusCode(200);
            response.setMessage("Review Updated Successfully");
            response.setReview(reviewDTO);

        }catch (CustomException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Updating the Review." + e.getMessage());
        }
        return  response;
    }

    @Override
    public Response deleteReview(Long reviewId ,  User authenticatedUser) {
        Response response = new Response();
        try {
             Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new CustomException("Review Id Not Found"));

             if(!review.getUser().equals(authenticatedUser) && !"ADMIN".equals(authenticatedUser.getRoles())){
                 response.setStatusCode(403); // Forbidden
                 response.setMessage("You are not authorized to delete this review.");
                 return response;
             }

             reviewRepository.deleteById(reviewId);

             response.setStatusCode(200);
             response.setMessage("Review Deleted Successfully.");

        }catch (CustomException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Deleting the Review." + e.getMessage());
        }
        return  response;
    }
}

