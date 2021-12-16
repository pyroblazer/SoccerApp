package com.example.soccerapp.API;

import com.squareup.moshi.Moshi;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    public String api_key = "15220497d14c46f5ba8cf13a3ccfe098";

    @GET("current")
    Call<Weather> getWeather(@Query("city") String city, @Query("key") String key);

    Moshi moshi = new Moshi.Builder().build();

    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://api.weatherbit.io/v2.0/")
            .build();

    WeatherApiService service = retrofit.create(WeatherApiService.class);
}
