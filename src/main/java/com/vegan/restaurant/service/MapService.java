package com.vegan.restaurant.service;

import com.vegan.restaurant.entity.Coordinate;
import com.vegan.restaurant.repository.CoordinateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * 장소 간의 거리, 네비게이션, 좌표 관련 비즈니스 로직
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MapService {

    private final static String TAG = "[MapService]";
    private final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    private final DataService dataService;
    private final CoordinateRepository coordinateRepository;

    /**
     *  좌표계 정보들을 DB에 삽입한다.
     */
    @Transactional
    public void insertCoordinates(List<Coordinate> coordinates) throws IOException {
        log.info("[{}} insertCoordinatesInformation() start", TAG);
        coordinateRepository.saveAll(coordinates);
    }


    /**
     * 사용자의 위치 <-> 목적지까지의 거리를 구한다.
     */
    public int calculateDistanceInKilometer(double userLat, double userLng, double venueLat, double venueLng) {
        log.info("[{}} calculateDistanceInKilometer() start", TAG);

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
    }
}
