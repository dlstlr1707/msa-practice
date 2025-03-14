package com.example.weather.service;

import com.example.weather.config.client.WeatherClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${weather.api.key}")
    private String accessKey;

    private final WeatherClient weatherClient;

    // Get 호출
    public String getWeather(
    ) {
        int pageNo = 1;
        int numOfNum = 12;
        String dataType = "json";
        String base_date = "20250306";
        String base_time = "1400";
        int nx = 60;
        int ny = 128;
        return weatherClient.getWeather(pageNo,numOfNum,dataType,base_date,base_time,nx,ny,accessKey);
    }
}
