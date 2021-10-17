package com.vegan.restaurant.service;

import com.vegan.restaurant.entity.Coordinate;
import com.vegan.restaurant.repository.CoordinateRepository;
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

/**
 * 장소 간의 거리, 네비게이션, 좌표 관련 비즈니스 로직
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MapService {
    private final static String TAG = "[MapService]";
    private final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
    private final static String COORDINATE_FILE_PATH = "/Users/iyunbog/Downloads/Study/NavigationDB/match_build_seoul2.txt";

    private final CoordinateRepository coordinateRepository;

    /**
     *  (1회용) 좌표계 정보를 DB에 삽입한다.
     */
    @Transactional
    public void insertCoordinatesInformation() throws IOException {
        log.info("[{}} insertCoordinatesInformation() start", TAG);
        List<Coordinate> coordinates = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(COORDINATE_FILE_PATH),
                16 * 1024
        );

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] arrLine = line.split("\\|");
            if(arrLine[23].isEmpty() || arrLine[24].isEmpty())
                continue;

            log.info("[{}} insertCoordinatesInformation() start, [{}]", TAG, Arrays.toString(arrLine));
            Coordinate coordinate = Coordinate.builder()
                    .si(arrLine[1])
                    .gu(arrLine[2])
                    .dong(arrLine[3])
                    .pathAddress(arrLine[5] + " " + arrLine[7])
                    .name(arrLine[11])
                    .category(arrLine[12])
                    .x(Double.parseDouble(arrLine[23]))
                    .y(Double.parseDouble(arrLine[24]))
                    .build();

            coordinates.add(coordinate);
        }

        bufferedReader.close();

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
