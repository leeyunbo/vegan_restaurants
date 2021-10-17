package com.vegan.restaurant.repository;

import com.vegan.restaurant.entity.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinateRepository extends JpaRepository<Coordinate, String> {
}
