package com.example.weather.config.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="weatherClient", url="${weather.api.url}")
public interface WeatherClient {
    @GetMapping
    String getWeather(
            @RequestParam int pageNo,
            @RequestParam int numOfNum,
            @RequestParam String dataType,
            @RequestParam String base_date,
            @RequestParam String base_time,
            @RequestParam int nx,
            @RequestParam int ny,
            @RequestParam String authKey);
}
