package com.example.soccerapp.API;

import com.squareup.moshi.Json;

public class WeatherDetail {
    @Json(name="description") String description;
    @Json(name="icon") String icon;

    public String getDescription() {
        return description;
    }

    public String getUrlIcon(){
//        String iconCode = icon.substring(1);
        return "https://www.weatherbit.io/static/img/icons/"+icon+".png";
    }
}
