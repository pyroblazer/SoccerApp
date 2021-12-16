package com.example.soccerapp.API;

import com.squareup.moshi.Moshi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ScheduleApiService {
    @GET("eventspastleague.php?id=4328")
    Call<Events> getSchedule();

    @GET("http://134.209.97.218:5050/api/v1/json/1/lookupteam.php")
    Call<Team> getTeams(@Query("id") String id);

    @GET("eventslast.php")
    Call<Results> getLastSchedule(@Query("id") String id);

    @GET("eventsnext.php")
    Call<Events> getUpcomingSchedule(@Query("id") String id);

    @GET("lookupevent.php")
    Call<DetailMatchEvents> getMatchDetail(@Query("id") String id);
//    @Query("id") String id

    Moshi moshi = new Moshi.Builder().build();

    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("http://134.209.97.218:5050/api/v1/json/1/")
            .build();

    ScheduleApiService service = retrofit.create(ScheduleApiService.class);
}
