package com.example.soccerapp.API;

import com.squareup.moshi.Json;

public class WeatherInfo {
    @Json(name="weather") WeatherDetail weatherDetail;
    @Json(name="temp") float temp;

    public WeatherDetail getWeatherDetail() {
        return weatherDetail;
    }

    public float getTemp() {
        return temp;
    }
}
