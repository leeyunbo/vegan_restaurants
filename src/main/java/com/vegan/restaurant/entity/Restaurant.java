package com.vegan.restaurant.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name="restaurants", indexes = {@Index(columnList = "name")})
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    private String name;

    @Column(length = 4096)
    private String description;

    private String category;

    private String telephone;

    private String address;

    @Setter
    private Double latitude;

    @Setter
    private Double longitude;

    @Builder
    public Restaurant(String name, String description, String category, String telephone, String address) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.telephone = telephone;
        this.address = address;
    }
}
