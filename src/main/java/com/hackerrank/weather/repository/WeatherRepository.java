package com.hackerrank.weather.repository;

import com.hackerrank.weather.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Integer> {

    Weather findByDate(Date date);

    List<Weather> findByCityIgnoreCase(String city);

    List<Weather> findAllByOrderByDateAsc();

    List<Weather> findAllByOrderByIdAsc();

    List<Weather> findAllByOrderByDateDesc();

}
