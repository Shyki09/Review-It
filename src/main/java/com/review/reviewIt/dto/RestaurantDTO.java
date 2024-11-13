package com.review.reviewIt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    private String cuisine;
    private String restaurantDescription;
    private List<ReviewDTO> reviews  = new ArrayList<>();
}
