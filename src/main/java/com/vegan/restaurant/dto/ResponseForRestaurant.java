package com.vegan.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class ResponseForRestaurant {
    Long id;
    String name;
    String description;
    String category;
    String telephone;
    double distance = -0.1;
}
