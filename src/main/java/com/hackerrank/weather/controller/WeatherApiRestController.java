package com.hackerrank.weather.controller;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    WeatherRepository weatherRepository;

    @PostMapping
    public ResponseEntity<Weather> createWeather(@RequestBody Weather weather) {

        weatherRepository.save(weather);
        return new ResponseEntity<>(weather, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Weather>> getWeathers(@RequestParam String date, @RequestParam String city, @RequestParam String sort) throws Exception {


        List<Weather> weatherList = weatherRepository.findAll();

        List<Weather> weathers = new ArrayList<>();
        if (date != null || city != null || sort != null) {
            if (date != null) {
                for (Weather weather : weatherList) {
                    Date databaseDate = weather.getDate();
                    Date newDate = new SimpleDateFormat("YYYY-MM-DD").parse(date);
                    if (newDate.equals(databaseDate)) {
                        weathers.add(weather);
                    }
                }
            }
            if (city != null) {
                for (Weather weather : weathers) {
                    String databaseCity = weather.getCity();
                    if (city.equalsIgnoreCase(databaseCity)) {
                        weathers.add(weather);
                    }
                }
            }
            if (sort != null) {
                if (sort.equalsIgnoreCase("date")) {
                    Comparator<Weather> ascendingOrder = (w1, w2) -> {
                        return Long.valueOf(w1.getDate().getTime()).compareTo(w2.getDate().getTime());
                    };
                    weatherList.addAll(weathers);
                    Collections.sort(weathers, ascendingOrder);
                }
                if (sort.equalsIgnoreCase("-date")) {
                    Comparator<Weather> descendingOrder = (w1, w2) -> {
                        return w1.getDate().compareTo(w2.getDate());
                    };
                    weatherList.addAll(weathers);
                    Collections.sort(weathers, descendingOrder);
                }

            }
            return ResponseEntity.ok().body(weathers);

        }
        weathers = weatherRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return ResponseEntity.ok().body(weathers);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWeatherById(@PathVariable Integer id) {

        if (weatherRepository.existsById(id)) {
            Weather weather = weatherRepository.findById(id).get();
            return ResponseEntity.ok().body(weather);
        } else {
            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
            return responseEntity;
        }
    }
}








