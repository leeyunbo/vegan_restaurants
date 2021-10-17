package com.vegan.restaurant.entity;


import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name="coordinates", indexes = {@Index(columnList = "pathAddress")})
public class Coordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coordinate_id")
    private Long id;
    private String si;
    private String gu;
    private String dong;
    private String pathAddress;
    private String name;
    private String category;
    private Double x;
    private Double y;

    @Builder
    public Coordinate(String si, String gu, String dong, String pathAddress, String name, String category, Double x, Double y) {
        this.si = si;
        this.gu = gu;
        this.dong = dong;
        this.pathAddress = pathAddress;
        this.name = name;
        this.category = category;
        this.x = x;
        this.y = y;
    }

    @Builder
    public Coordinate(String address, String pathAddress, String name, String category, Double x, Double y) {

    }
}
