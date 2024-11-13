package com.review.reviewIt.controller;

import com.review.reviewIt.entity.Response;
import com.review.reviewIt.service.iterfac.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;


    @PostMapping("/add-restaurant")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> addRestaurant ( @RequestParam(value = "name",required = false) String name,
                                                    @RequestParam(value = "address",required = false)String address,
                                                    @RequestParam(value = "cuisine" ,required = false) String cuisine,
                                                    @RequestParam(value = "restaurantDescription" , required = false)String restaurantDescription){
        if (name == null || name.isEmpty() ||
                address == null || address.isBlank() ||
                cuisine == null || cuisine.isBlank()) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please Provide for all fields(name , address , cuisine)");

            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        Response response = restaurantService.addRestaurant( name,  address,  cuisine,  restaurantDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{restaurantId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateRestaurant(@PathVariable Long restaurantId,
                                                     @RequestParam(value = "name" , required = false) String name,
                                                     @RequestParam(value = "address" , required = false)String address,
                                                     @RequestParam(value = "cuisine" , required = false) String cuisine,
                                                     @RequestParam(value = "restaurantDescription" , required = false)String restaurantDescription){
        Response response = restaurantService.updateRestaurant(restaurantId , name , address,cuisine, restaurantDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{restaurantId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteRestaurant(@PathVariable Long restaurantId){
        Response response = restaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @GetMapping("/restaurant-by-id/{restaurantId}")
    public ResponseEntity<Response> getRestaurantById(@PathVariable Long restaurantId) {
        Response response = restaurantService.getRestaurantById(restaurantId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRestaurants() {
        Response response = restaurantService.getAllRestaurant();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/restaurant-reviews/{restaurantId}")
    public ResponseEntity<Response> getRestaurantReviews(@PathVariable Long restaurantId) {
        Response response = restaurantService.getRestaurantReviews(restaurantId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/restaurant-by-name/{name}")
    public ResponseEntity<Response> getRestaurantByName(@PathVariable String name) {
        Response response = restaurantService.getRestaurantByName(name);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }




}







