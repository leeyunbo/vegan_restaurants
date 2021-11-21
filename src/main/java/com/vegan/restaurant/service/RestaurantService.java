package com.vegan.restaurant.service;

import com.vegan.restaurant.dto.ResponseForRestaurant;
import com.vegan.restaurant.entity.Coordinate;
import com.vegan.restaurant.entity.Restaurant;
import com.vegan.restaurant.repository.CoordinateRepository;
import com.vegan.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public List<ResponseForRestaurant> findRestaurants(int pageNumber, int pageSize, String location) {
        Double[] convertedToUTMKLocation = mapService.convertToUTMKLocation(location);
        List<Restaurant> restaurants = restaurantRepository.findAll();

        List<ResponseForRestaurant> responseForRestaurants = new ArrayList<>();

        for(Restaurant restaurant : restaurants) {
            if(convertedToUTMKLocation[0] == null || convertedToUTMKLocation[1] == null) break;
            Double distance = mapService.calculateDistanceInKilometer(convertedToUTMKLocation[0], convertedToUTMKLocation[1], restaurant.getLatitude(), restaurant.getLongitude());
            responseForRestaurants.add(new ResponseForRestaurant(
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getDescription(),
                    restaurant.getCategory(),
                    restaurant.getTelephone(),
                    distance
            ));
        }

        responseForRestaurants.sort(Comparator.comparingDouble(ResponseForRestaurant::getDistance));

        return responseForRestaurants;
    }

    public ResponseForRestaurant findRestaurant(Long id, String location) {
        Double[] convertedToUTMKLocation = mapService.convertToUTMKLocation(location);
        Restaurant restaurant = restaurantRepository.findById(id).get();

        Double distance = mapService.calculateDistanceInKilometer(convertedToUTMKLocation[0], convertedToUTMKLocation[1], restaurant.getLatitude(), restaurant.getLongitude());

        return ResponseForRestaurant.builder()
                .id(restaurant.getId())
                .category(restaurant.getCategory())
                .description(restaurant.getDescription())
                .distance(distance)
                .telephone(restaurant.getTelephone())
                .name(restaurant.getName())
                .build();
    }

    @Transactional
    public void insertRestaurants(List<Restaurant> restaurants) throws IOException {
        for(Restaurant restaurant : restaurants) {
            setLatLon(restaurant);
        }

        restaurantRepository.saveAll(restaurants);
    }

    @Transactional
    public Restaurant insertRestaurant(Restaurant restaurant) {
        setLatLon(restaurant);
        return restaurantRepository.save(restaurant);
    }

    private void setLatLon(Restaurant restaurant) {
        String[] arrAddress = restaurant.getAddress().split(" ");
        Optional<Coordinate> coordinate = coordinateRepository.findFirstByPathAddressContains(
                arrAddress[2] + " " + arrAddress[3]
        );

        if (coordinate.isPresent()) {
            restaurant.setLatitude(coordinate.get().getX());
            restaurant.setLongitude(coordinate.get().getY());
        }
    }
}
