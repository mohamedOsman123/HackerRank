package com.hackerrank.weather.controller;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(path = "/weather")
public class WeatherApiRestController {

    @Autowired
    WeatherRepository weatherRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    SimpleDateFormat format;

    @PostMapping
    public ResponseEntity<Weather> createWeather(@RequestBody Weather weather) throws ParseException {

        weather.setId(null);
        Weather returnedWeather=weatherRepository.save(weather);
        return new ResponseEntity<>(returnedWeather, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Weather>> getWeathers(@RequestParam( required = false) String date, @RequestParam( required = false) String city, @RequestParam( required = false) String sort) throws Exception {


        List<Weather> weatherList = weatherRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        List<Weather> weathers = new ArrayList<>();
        if (date != null || city != null || sort != null) {
            if (date != null) {
                for (Weather weather : weatherList) {
                    String  databaseDate = weather.getDate().toString();
                    Date newDatabaseDate = format.parse(databaseDate);
                    Date newDate = format.parse(date);
                    if (newDatabaseDate.equals(newDate)) {
                        weathers.add(weather);
                    }
                }
            }
            if (city != null) {
                List<String> cityList=new ArrayList<>(Arrays.asList(city.split(",")));
                for (String returnCity : cityList) {
                    List<Weather> returnedWeathers=weatherRepository.findByCityIgnoreCase(returnCity);
                    if (!returnedWeathers.isEmpty()) {
                        weathers.addAll(returnedWeathers);
                    }
                }
                Comparator<Weather> idAscendingOrder = (w1, w2) -> {
                    return Integer.valueOf(w1.getId()).compareTo(w2.getId());
                };
                Collections.sort(weathers,idAscendingOrder);

            }
            if (sort != null) {
                if (sort.equalsIgnoreCase("date")) {
                    weathers=weatherRepository.findAllByOrderByDateAsc();

                }
                if (sort.equalsIgnoreCase("-date")) {
                    weathers=weatherRepository.findAllByOrderByDateDesc();
                }

            }
            return ResponseEntity.ok().body(weathers);

        }
        else {
            weathers = weatherRepository.findAllByOrderByIdAsc();
            return ResponseEntity.ok().body(weathers);
        }

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








