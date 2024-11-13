package com.review.reviewIt.controller;

import com.review.reviewIt.entity.Response;
import com.review.reviewIt.entity.User;
import com.review.reviewIt.service.iterfac.ReviewService;
import com.review.reviewIt.service.iterfac.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final UserService userService;
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService , UserService userService) {
        this.reviewService = reviewService;
        this.userService  = userService;

    }


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> addReview(
            @RequestParam(value = "restaurantId" , required = false) Long restaurantId,
            @RequestParam(value = "rating" , required = false) Long rating,
            @RequestParam(value = "reviewText" , required = false)String reviewText
    ) {
        //Validation
        if (restaurantId == null || rating == null || reviewText == null || reviewText.isBlank()){
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please Provide for all fields(restaurantId ,Rating,Review)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        //User Authentication Logic
        User authenticatedUser = userService.getAuthenticated();

        Response response = reviewService.addReview(restaurantId , rating , reviewText , authenticatedUser);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @GetMapping("/get-review-by-Id/{reviewId}")
    public ResponseEntity<Response> getReviewByID(@PathVariable Long reviewId){
        Response response = reviewService.getReviewById(reviewId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @PutMapping("update-review/{reviewId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> updateReview(@PathVariable Long reviewId,
                                                 @RequestParam(value = "rating" , required = false) Long rating,
                                                 @RequestParam(value = "reviewText" , required = false) String reviewText){

        //User Authentication Logic
        User authenticatedUser = userService.getAuthenticated();

        // Input Validation: Ensure that at least one field is provided for update
        if (rating == null && (reviewText == null || reviewText.isBlank())) {
            Response response = new Response();
            response.setStatusCode(400); // Bad Request
            response.setMessage("Please provide either a new rating or review text to update.");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        Response response = reviewService.updateReview(reviewId,rating , reviewText, authenticatedUser );
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @DeleteMapping("/delete/{reviewId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> deleteReview(@PathVariable Long reviewId){
        //User Authentication Logic
        User authenticatedUser = userService.getAuthenticated();

        Response response = reviewService.deleteReview(reviewId , authenticatedUser);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
