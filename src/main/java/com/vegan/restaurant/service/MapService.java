package com.vegan.restaurant.service;

import com.vegan.restaurant.entity.Coordinate;
import com.vegan.restaurant.repository.CoordinateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final static double MAX_DISTANCE = 987654321.0;

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
     * 좌표계 정보들을 가져온다.
     */
    public ResponseEntity<?> getCoordinates(int pageNumber, int pageSize, String dong) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Coordinate> coordinatePage = coordinateRepository.findCoordinatesByDong(dong, pageable);
        return new ResponseEntity<>(coordinatePage, HttpStatus.OK);
    }

    /**
     * 사용자의 위치 <-> 목적지까지의 거리를 구한다.
     */
    public Double calculateDistanceInKilometer(Double userLat, Double userLng, Double venueLat, Double venueLng) {
        if(venueLat == null || venueLng == null) return MAX_DISTANCE;
        return Math.round(Math.sqrt((userLat-venueLat) * (userLat-venueLat) + (userLng-venueLng) * (userLng-venueLng)) / 1000 * 100) / 100.0;
    }

    public Double[] convertToUTMKLocation(String location) {
        String[] arrLocation = location.split("\\|");
        return new Double[] {Double.parseDouble(arrLocation[0]), Double.parseDouble(arrLocation[1])};
    }
}
