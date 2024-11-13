package com.review.reviewIt.repository;

import com.review.reviewIt.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review , Long> {
    List<Review> findByRestaurantId(Long restaurantId);
}
