package com.vegan.restaurant.controller;

import com.vegan.restaurant.entity.Restaurant;
import com.vegan.restaurant.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerTest {

    @Autowired
    RestaurantService restaurantService;

    @Test
    public void beanValidation() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Restaurant restaurant = new Restaurant(" ", "description", " ", "", " ");

        Set<ConstraintViolation<Restaurant>> violationSet = validator.validate(restaurant);

        for (ConstraintViolation<Restaurant> restaurantConstraintViolation : violationSet) {
            System.out.println("restaurantConstraintViolation = " + restaurantConstraintViolation);
        }
    }

}