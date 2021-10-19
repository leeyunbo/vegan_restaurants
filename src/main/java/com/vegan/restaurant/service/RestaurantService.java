package com.vegan.restaurant.service;

import com.vegan.restaurant.dto.ResponseForRestaurant;
import com.vegan.restaurant.entity.Coordinate;
import com.vegan.restaurant.entity.Restaurant;
import com.vegan.restaurant.repository.CoordinateRepository;
import com.vegan.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CoordinateRepository coordinateRepository;
    private final MapService mapService;
    private final DataService dataService;

    private final static String TAG = "[RestaurantService]";

    public List<ResponseForRestaurant> findRestaurants(int pageNumber, int pageSize, String location) {
        Double[] convertedToUTMKLocation = mapService.convertToGpsLocation(location);
        List<Restaurant> restaurants = restaurantRepository.findAll();

        List<ResponseForRestaurant> responseForRestaurants = new ArrayList<>();

        for(Restaurant restaurant : restaurants) {
            if(convertedToUTMKLocation[0] == null || convertedToUTMKLocation[1] == null) break;
            double distance = mapService.calculateDistanceInKilometer(convertedToUTMKLocation[0], convertedToUTMKLocation[1], restaurant.getLatitude(), restaurant.getLongitude());
            responseForRestaurants.add(new ResponseForRestaurant(
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getDescription(),
                    restaurant.getCategory(),
                    restaurant.getTelephone(),
                    distance
            ));
        }

        responseForRestaurants.sort((o1, o2) -> Double.compare(o2.getDistance(), o1.getDistance()));

        return responseForRestaurants;
    }

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
