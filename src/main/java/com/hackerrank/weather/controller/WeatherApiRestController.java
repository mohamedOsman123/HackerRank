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
    public ResponseEntity<List<Weather>> getWeathers(@RequestParam String date) throws Exception {

        if (date != null) {
            for (Weather weather : weathers) {
                Date databaseDate = weather.getDate();
                Date newDate = new SimpleDateFormat("YYYY-MM-DD").parse(date);
                if (newDate.equals(databaseDate)) {
                    List<Weather> weathers = new ArrayList();
                    weathers.add(weather);
                    return ResponseEntity.ok().body(weathers);
                }
            }
        }
        List<Weather> weatherList = weathers.stream().sorted().collect(Collectors.toList());
        return ResponseEntity.ok().body(weatherList);

    }
}






