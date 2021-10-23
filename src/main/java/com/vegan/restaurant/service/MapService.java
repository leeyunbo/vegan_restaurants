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
     * 사용자의 위치 <-> 목적지까지의 거리를 구한다.
     */
    public Double calculateDistanceInKilometer(Double userLat, Double userLng, Double venueLat, Double venueLng) {
        if(venueLat == null || venueLng == null) return MAX_DISTANCE;
        return Math.sqrt((userLat-venueLat) * (userLat-venueLat) + (userLng-venueLng) * (userLng-venueLng)) / 100000;
    }

    public Double[] convertToGpsLocation(String location) {
        String[] arrLocation = location.split("\\|");

//        CRSFactory crsFactory = new CRSFactory();
//        CoordinateReferenceSystem WGS84 = crsFactory.createFromParameters("WGS84",
//                "+proj=longlat +datum=WGS84 +no_defs");
//        CoordinateReferenceSystem UTMK = crsFactory.createFromParameters("UTMK",
//                "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs");
//
//        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
//        CoordinateTransform wgsToUtm = ctFactory.createTransform(WGS84, UTMK);
//        ProjCoordinate result = new ProjCoordinate();
//        wgsToUtm.transform(new ProjCoordinate(Double.parseDouble(arrLocation[0]), Double.parseDouble(arrLocation[1])), result);
//
//        log.info("[{}], [{}]", TAG, result.x + "," + result.y);

        return new Double[] {Double.parseDouble(arrLocation[0]), Double.parseDouble(arrLocation[1]
        )};
    }
}
