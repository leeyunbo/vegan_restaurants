package com.vegan.restaurant.service;

import com.vegan.restaurant.entity.Coordinate;
import com.vegan.restaurant.entity.Restaurant;
import com.vegan.restaurant.repository.CoordinateRepository;
import com.vegan.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CoordinateRepository coordinateRepository;
    private final DataService dataService;

    private final static String TAG = "[RestaurantService]";

    @Transactional
    public void insertRestaurants(List<Restaurant> restaurants) throws IOException {
        for(Restaurant restaurant : restaurants) {
            String[] arrAddress = restaurant.getAddress().split(" ");
            Optional<Coordinate> coordinate = coordinateRepository.findFirstByPathAddressContains(
                    arrAddress[2] + " " + arrAddress[3]
            );

            if (coordinate.isPresent()) {
                restaurant.setLatitude(coordinate.get().getX());
                restaurant.setLongitude(coordinate.get().getY());
            }
        }

        restaurantRepository.saveAll(restaurants);
    }
}
