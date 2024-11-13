package com.review.reviewIt.service.iterfac;

import com.review.reviewIt.entity.Response;

public interface RestaurantService {

    Response addRestaurant (String name, String address, String cuisine , String restaurantDescription);

    Response getRestaurantById(Long id);

    Response getAllRestaurant();

    Response getRestaurantReviews(Long restaurantId);

    Response getRestaurantByName(String name);

    Response updateRestaurant(Long restaurantId, String name, String address, String cuisine , String restaurantDescription);

    Response deleteRestaurant(Long restaurantId);


}
