package com.hackerrank.weather.controller;

import com.hackerrank.weather.model.Weather;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(path = "/weather")
public class WeatherApiRestController {


    //static List<Weather> weathers=new ArrayList<>();

    static Map<Integer,Weather> weathers=new HashMap();
    @PostMapping
    public ResponseEntity<Weather> insertWeather(@RequestBody Weather weather) {

        Integer uniqueId = new Integer(42);
        weather.setId(uniqueId);
       weathers.put(uniqueId,weather);
        return new ResponseEntity<>(weather, HttpStatus.CREATED);
    }
}

