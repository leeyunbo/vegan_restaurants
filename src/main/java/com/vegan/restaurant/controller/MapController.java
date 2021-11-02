package com.vegan.restaurant.controller;

import com.vegan.restaurant.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/v1/map")
@RestController
public class MapController {

    private final MapService mapService;

    @GetMapping("/coordinates")
    public ResponseEntity<?> getCoordinates(@RequestParam String dong,
                                            @RequestParam(name="page_size") int pageSize,
                                            @RequestParam(name="page_number") int pageNumber) {
        return mapService.getCoordinates(pageNumber, pageSize, dong);
    }
}
