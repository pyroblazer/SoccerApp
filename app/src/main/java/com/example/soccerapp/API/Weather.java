package com.example.soccerapp.API;

import com.squareup.moshi.Json;

public class Weather {
    @Json(name="data") WeatherInfo[] weather;

    public WeatherInfo[] getWeather() {
        return weather;
    }
}
