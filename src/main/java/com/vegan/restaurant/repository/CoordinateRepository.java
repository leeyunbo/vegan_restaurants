package com.vegan.restaurant.repository;

import com.vegan.restaurant.entity.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoordinateRepository extends JpaRepository<Coordinate, String> {

    Optional<Coordinate> findFirstByPathAddressContains(String pathAddress);
}
