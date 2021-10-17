package com.vegan.restaurant.controller;

import com.vegan.restaurant.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/coordinates")
@RestController
public class DataController {

    private final static String TAG = "[DataController]";

    private final MapService mapService;

    @GetMapping
    public ResponseEntity<?> addCoordinates() throws IOException {
        log.info("[{}} addCoordinates() start", TAG);

        try {
            mapService.insertCoordinatesInformation();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
