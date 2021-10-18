package com.vegan.restaurant.controller;

import com.vegan.restaurant.entity.Coordinate;
import com.vegan.restaurant.entity.Restaurant;
import com.vegan.restaurant.service.DataService;
import com.vegan.restaurant.service.MapService;
import com.vegan.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/data")
@RestController
public class DataController {

    private final static String TAG = "[DataController]";

    private final MapService mapService;
    private final DataService dataService;
    private final RestaurantService restaurantService;

    /**
     * 텍스트 파일에 입력된 좌표계 정보를 DB에 저장합니다.
     */
    @GetMapping("/coordinates")
    public ResponseEntity<?> addCoordinates(@RequestParam(name="file_path") String filePath) {
        log.info("[{}} addCoordinates() start", TAG);

        try {
            List<Coordinate> coordinates = dataService.findCoordinatesByTxtFile(filePath);
            mapService.insertCoordinates(coordinates);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 텍스트 파일에 입력된 비건 레스토랑 정보를 DB에 저장합니다.
     */
    @GetMapping("/restaurants")
    public ResponseEntity<?> addRestaurants(@RequestParam(name="file_path") String filePath) {
        log.info("[{}} addRestaurants() start", TAG);

        try {
            List<Restaurant> restaurants = dataService.findRestaurantsByTxtFile(filePath);
            restaurantService.insertRestaurants(restaurants);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 카카오 맵 URL의 비건 레스토랑 정보를 크롤링을 통해 가져옵니다.
     */
    @GetMapping("/crawling/restaurants")
    public ResponseEntity<?> crawlingRestaurants(@RequestParam(name="url") String url) {
        log.info("[{}} crawlingRestaurants() start", TAG);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
