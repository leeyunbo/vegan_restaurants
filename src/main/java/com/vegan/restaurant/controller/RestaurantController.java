package com.vegan.restaurant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurant")
@RestController
public class RestaurantController {

    @GetMapping
    public ResponseEntity<?> hello() {
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }
}
