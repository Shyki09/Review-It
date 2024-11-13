package com.review.reviewIt.repository;

import com.review.reviewIt.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant , Long> {
    Optional<Restaurant> findByName(String name);
}
