package com.hackerrank.weather.controller;

import com.hackerrank.weather.model.Weather;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/weather")
public class WeatherApiRestController {


    static List<Weather> weathers = new ArrayList();

    @PostMapping
    public ResponseEntity<Weather> createWeather(@RequestBody Weather weather) {

        Integer uniqueId = new Integer(42);
        weather.setId(uniqueId);
        weathers.add(weather);
        return new ResponseEntity<>(weather, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Weather>> getWeathers(@RequestParam String date,@RequestParam String city,@RequestParam String sort) throws Exception {


        List<Weather> weatherList = new ArrayList();

        if (date!=null || city!=null || sort!=null) {
            if (date != null) {
                for (Weather weather : weathers) {
                    Date databaseDate = weather.getDate();
                    Date newDate = new SimpleDateFormat("YYYY-MM-DD").parse(date);
                    if (newDate.equals(databaseDate)) {
                        weatherList.add(weather);
                    }
                }
            }
            if (city != null) {
                for (Weather weather : weathers) {
                    String databaseCity = weather.getCity();
                    if (city.equalsIgnoreCase(databaseCity)) {
                        weatherList.add(weather);
                    }
                }
            }
           /* if (sort != null) {
                if (sort=="date"){
                    Comparator<Weather> comparator = (w1, w2) -> {
                        return Long.valueOf(w1.getCreatedOn().getTime()).compareTo(w2.getCreatedOn().getTime());
                    };
                    Collections.sort(list, comparator);
            }*/
            return ResponseEntity.ok().body(weatherList);
        }
        List<Weather> allWeathers = weathers.stream().sorted().collect(Collectors.toList());
        return ResponseEntity.ok().body(allWeathers);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWeatherById(@PathVariable Integer id) {

        for (Weather weather : weathers) {
            if (weather.getId() == id) {
                return ResponseEntity.ok().body(weather);
            }
            }
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
        return responseEntity;
        }
    }








