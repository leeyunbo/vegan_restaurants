package com.vegan.restaurant.service;

import com.vegan.restaurant.entity.Coordinate;
import com.vegan.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataService {

    private final static String TAG = "[DataService]";

    public List<Restaurant> findRestaurantsByTxtFile(String filePath) throws IOException {
        List<Restaurant> restaurants = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(filePath),
                16 * 1024
        );

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] arrLine = line.split("\t");
            Restaurant restaurant = Restaurant.builder()
                    .name(arrLine[1])
                    .category(arrLine[2])
                    .telephone(arrLine[3])
                    .address(arrLine[5])
                    .description(arrLine[6])
                    .build();

            restaurants.add(restaurant);
        }

        return restaurants;
    }

    public List<Coordinate> findCoordinatesByTxtFile(String filePath) throws IOException {
        List<Coordinate> coordinates = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(filePath),
                16 * 1024
        );

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] arrLine = line.split("\\|");
            if(arrLine[23].isEmpty() || arrLine[24].isEmpty())
                continue;

            Coordinate coordinate = Coordinate.builder()
                    .si(arrLine[1])
                    .gu(arrLine[2])
                    .dong(arrLine[3])
                    .pathAddress(arrLine[5] + " " + arrLine[7])
                    .name(arrLine[11])
                    .category(arrLine[12])
                    .x(Double.parseDouble(arrLine[23]))
                    .y(Double.parseDouble(arrLine[24]))
                    .build();

            coordinates.add(coordinate);
        }

        return coordinates;
    }

    public List<Restaurant> findRestaurantsByCrawling(String url) throws IOException {
        log.info("[{}} findRestaurantsByCrawling() start", TAG);

        List<Restaurant> restaurants = new ArrayList<>();

        Document document = Jsoup.connect(url).get();
        Elements contents = document.getElementsByClass("FavoriteDetailItem");

        for (Element content : contents) {
            String name = content.getElementsByClass("link_txt").get(0).text();
            String pathAddress = content.getElementsByClass("desc_region").get(0).text();
            Restaurant restaurant = Restaurant.builder()
                    .name(name)
                    .address(pathAddress)
                    .build();

            restaurants.add(restaurant);
        }

        return restaurants;
    }

}
