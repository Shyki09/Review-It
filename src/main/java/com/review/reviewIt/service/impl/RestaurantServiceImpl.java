package com.review.reviewIt.service.impl;

import com.review.reviewIt.entity.Response;
import com.review.reviewIt.dto.RestaurantDTO;
import com.review.reviewIt.entity.Restaurant;
import com.review.reviewIt.exception.CustomException;
import com.review.reviewIt.repository.RestaurantRepository;
import com.review.reviewIt.service.iterfac.RestaurantService;
import com.review.reviewIt.utils.Utils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }


    @Override
    @Transactional
    public Response addRestaurant(String name, String address, String cuisine, String restaurantDescription) {
        Response response = new Response();

        try {
            Optional<Restaurant> existingRestaurant = restaurantRepository.findByName(name);
            if(existingRestaurant.isPresent()){
                response.setStatusCode(400);  // Bad request
                response.setMessage("A restaurant with the same name already exists");
                return response;
            }

            Restaurant restaurant = new Restaurant();
            restaurant.setName(name);
            restaurant.setAddress(address);
            restaurant.setCuisine(cuisine);
            restaurant.setRestaurantDescription(restaurantDescription);


            Restaurant savedRestaurant =restaurantRepository.save(restaurant);
            RestaurantDTO restaurantDTO =  Utils.mapRestaurantEntityToRestaurantDTO(savedRestaurant);
            response.setStatusCode(200);
            response.setMessage("Restaurant Added Successfully");
            response.setRestaurant(restaurantDTO);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Adding a Restaurant" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRestaurantById(Long restaurantId) {
        Response response = new Response();

        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(()-> new CustomException("Restaurant not Found"));
            RestaurantDTO restaurantDTO = Utils.mapRestaurantEntityToRestaurantDTO(restaurant);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRestaurant(restaurantDTO);

        }catch (CustomException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Getting the Restaurant By Id" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllRestaurant() {
        Response response = new Response();

        try {
            List<Restaurant> restaurantList = restaurantRepository.findAll(Sort.by(Sort.Direction.ASC , "id"));
            List<RestaurantDTO> restaurantDTOList = Utils.mapRestaurantListEntityToRestaurantListDTO(restaurantList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRestaurantList(restaurantDTOList);
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Getting All Restaurants" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRestaurantReviews(Long restaurantId) {
        Response response = new Response();

        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(()-> new CustomException("Restaurant not Found"));

            if(restaurant.getReviews() == null || restaurant.getReviews().isEmpty()){
                response.setStatusCode(204); // No Content
                response.setMessage("Restaurant has no reviews.");
                return response;
            }
            RestaurantDTO restaurantDTO = Utils.mapRestaurantEntityToRestaurantDTOPlusReviews(restaurant);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRestaurant(restaurantDTO);


        }catch (CustomException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Getting the Review" + e.getMessage());
        }
        return response;
    }


    @Override
    public Response getRestaurantByName(String name) {
       Response response = new Response();
       try{
           Restaurant restaurant = restaurantRepository.findByName(name).orElseThrow(()-> new CustomException("Restaurant not Found"));
           RestaurantDTO restaurantDTO = Utils.mapRestaurantEntityToRestaurantDTO(restaurant);
           response.setStatusCode(200);
           response.setMessage("Successful");
           response.setRestaurant(restaurantDTO);

       }catch (CustomException e){
           response.setStatusCode(404);
           response.setMessage(e.getMessage());

       }catch (Exception e){
           response.setStatusCode(500);
           response.setMessage("Error Getting the Restaurant" + e.getMessage());
       }
        return response;
    }

    @Override
    public Response updateRestaurant(Long restaurantId, String name, String address, String cuisine, String restaurantDescription) {
       Response response = new Response();

        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new CustomException("Restaurant not Found"));
            if (name != null) restaurant.setName(name);
            if (address != null) restaurant.setAddress(address);
            if (cuisine != null) restaurant.setCuisine(cuisine);
            if (restaurantDescription != null) restaurant.setRestaurantDescription(restaurantDescription);

            Restaurant updateRestaurant = restaurantRepository.save(restaurant);
            RestaurantDTO restaurantDTO = Utils.mapRestaurantEntityToRestaurantDTO(updateRestaurant);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRestaurant(restaurantDTO);

        }catch(CustomException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Updating the Restaurant" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRestaurant(Long restaurantId) {
        Response response = new Response();

        try{
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new CustomException("Restaurant not Found"));
            restaurantRepository.deleteById(restaurantId);
            response.setStatusCode(200);
            response.setMessage("Successful");

        }catch(CustomException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Deleting the Restaurant" + e.getMessage());
        }
        return response;
    }
}
