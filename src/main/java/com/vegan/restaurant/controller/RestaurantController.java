package com.vegan.restaurant.controller;

import com.vegan.restaurant.dto.ResponseForRestaurant;
import com.vegan.restaurant.entity.Restaurant;
import com.vegan.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/restaurants")
@Controller
public class RestaurantController {

    private final RestaurantService restaurantService;

    private static final String TAG = "[RestaurantController]";

    /**
     *  비건 식당 조회
     */
    @GetMapping
    public String findVeganRestaurants( @RequestParam(name="page_number", defaultValue = "1") int pageNumber,
                                       @RequestParam(name="page_size", defaultValue = "30") int pageSize,
                                       @CookieValue(name = "location") String location,
                                       Model model) {
        log.info("[{}} findVeganRestaurants() start", TAG);

        List<ResponseForRestaurant> responseForRestaurants = restaurantService.findRestaurants(pageNumber, pageSize, location);
        model.addAttribute("restaurants", responseForRestaurants);

        return "restaurant-list";
    }
}
