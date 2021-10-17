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

    private final static String TAG = "[RestaurantService]";
    private static final String RESTAURANT_FILE_PATH = "/Users/iyunbog/Downloads/Study/vegan_food_list2.txt";

    @Transactional
    public void insertRestaurantsInformation() throws IOException {
        List<Restaurant> restaurants = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(RESTAURANT_FILE_PATH),
                16 * 1024
        );

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] arrLine = line.split("\t");
            log.info("[{}} insertRestaurantsInformation() start, [{}]", TAG, Arrays.toString(arrLine));
            Restaurant restaurant = Restaurant.builder()
                    .name(arrLine[1])
                    .category(arrLine[2])
                    .telephone(arrLine[3])
                    .address(arrLine[5])
                    .description(arrLine[6])
                    .build();

            String[] arrAddress = restaurant.getAddress().split(" ");
            Optional<Coordinate> coordinate = coordinateRepository.findFirstByPathAddressContains(
                    arrAddress[2] + " " + arrAddress[3]
            );

            if(coordinate.isPresent()) {
                restaurant.setLatitude(coordinate.get().getX());
                restaurant.setLongitude(coordinate.get().getY());
            }

            restaurants.add(restaurant);
        }

        restaurantRepository.saveAll(restaurants);
    }
}
