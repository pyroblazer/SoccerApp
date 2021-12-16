package com.example.soccerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soccerapp.API.DetailMatch;
import com.example.soccerapp.API.DetailMatchEvents;
import com.example.soccerapp.API.DetailTeam;
import com.example.soccerapp.API.ScheduleApiService;
import com.example.soccerapp.API.Team;
import com.example.soccerapp.API.Weather;
import com.example.soccerapp.API.WeatherApiService;
import com.example.soccerapp.API.WeatherDetail;
import com.example.soccerapp.API.WeatherInfo;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class MatchDetail extends AppCompatActivity {
    private TextView tvLeagueEvent;
    private TextView tvDateMatch;
    private TextView tvFullTime;
    private ImageView ivHomeBadgeTeam;
    private ImageView ivAwayBadgeTeam;
    private TextView tvHomeScore;
    private TextView tvAwayScore;
    private TextView tvHomeTeam;
    private TextView tvAwayTeam;
    private View line1;
    private TextView tvHomeGoals;
    private TextView tvAwayGoals;
    private ImageView ivBall;
    private View line2;
    private TextView tvShotOnTarget;
    private TextView tvHomeShot;
    private TextView tvAwayShot;
    private TextView tvWeatherInfo;
    private String[] homeGoals;
    private String[] awayGoals;
    private String strStadium;
    private String strStadiumLocation;
    private String idHomeTeam;
    private String idAwayTeam;
    private ImageView ivWeatherInfo;
    private WeatherInfo weatherInfo;
    private WeatherDetail[] weatherDetails;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Initialization
        tvLeagueEvent = (TextView) findViewById(R.id.match_detail_league_event);
        tvDateMatch = (TextView) findViewById(R.id.match_detail_date_match_event);
        tvFullTime = (TextView) findViewById(R.id.match_detail_full_time);
        ivHomeBadgeTeam = (ImageView) findViewById(R.id.match_detail_home_badge_team);
        ivAwayBadgeTeam = (ImageView) findViewById(R.id.match_detail_badge_away_team);
        tvHomeScore = (TextView) findViewById(R.id.match_detail_score_home_team);
        tvAwayScore = (TextView) findViewById(R.id.match_detail_score_away_team);
        tvHomeTeam = (TextView) findViewById(R.id.match_detail_home_team);
        tvAwayTeam = (TextView) findViewById(R.id.match_detail_away_team);
        line1 = (View) findViewById(R.id.lines1);
        tvHomeGoals = (TextView) findViewById(R.id.match_detail_home_goals);
        tvAwayGoals = (TextView) findViewById(R.id.match_detail_away_goals);
        ivBall = (ImageView) findViewById(R.id.match_detail_ball);
        line2 = (View) findViewById(R.id.lines2);
        tvShotOnTarget = (TextView) findViewById(R.id.match_detail_shotontarget);
        tvHomeShot = (TextView) findViewById(R.id.match_detail_home_shot);
        tvAwayShot = (TextView) findViewById(R.id.match_detail_away_shot);
        tvWeatherInfo = (TextView) findViewById(R.id.weather_info);
        ivWeatherInfo = (ImageView) findViewById(R.id.weather_icon);


        //Get Intent
        Intent getEventID = getIntent();
        String eventID = (String) getEventID.getExtras().get("eventID");
        final Boolean isMatched = (Boolean) getEventID.getExtras().get("isMatched");
        Log.d("Team ID",eventID);

        //Call
        Call<DetailMatchEvents> call = ScheduleApiService.service.getMatchDetail(eventID);
        call.enqueue(new Callback<DetailMatchEvents>(){

            @Override
            public void onResponse(Call<DetailMatchEvents> call, Response<DetailMatchEvents> response) {
                response.body();
                final DetailMatch[] detailMatch = response.body().getEvents();
                idHomeTeam = detailMatch[0].getIdHomeTeam();
                idAwayTeam = detailMatch[0].getIdAwayTeam();
                //Get Badge Home Team
                Call<Team> call2 = ScheduleApiService.service.getTeams(detailMatch[0].getIdHomeTeam());
                call2.enqueue(new Callback<Team>() {
                    @Override
                    public void onResponse(Call<Team> call2, Response<Team> response2) {
                        response2.body();
                        detailMatch[0].setUrlHomeBadge(response2.body().getTeams()[0].getStrTeamBadge());
                        strStadium = response2.body().getTeams()[0].getStrStadium();
                        strStadiumLocation = response2.body().getTeams()[0].getStrStadiumLocation();
                        Log.d("strHomeStadium",strStadium);
                        Log.d("strHomeStadiumLocation", StadiumLocation(strStadiumLocation));
                        Picasso.get().load(detailMatch[0].getUrlHomeBadge()).into(ivHomeBadgeTeam);

                        Call<Weather> call4 = WeatherApiService.service.getWeather(StadiumLocation(strStadiumLocation), WeatherApiService.api_key);
                        call4.enqueue(new Callback<Weather>() {
                            @Override
                            public void onResponse(Call<Weather> call4, Response<Weather> response4) {
                                response4.body();
                                WeatherInfo[] temp = response4.body().getWeather();
                                tvWeatherInfo.setText(temp[0].getWeatherDetail().getDescription());
                                Log.d("weathericon",temp[0].getWeatherDetail().getUrlIcon());
                                Picasso.get().load(temp[0].getWeatherDetail().getUrlIcon()).into(ivWeatherInfo);

                            }

                            @Override
                            public void onFailure(Call<Weather> call4, Throwable t4) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Team> call2, Throwable t2) {
                    }
                });
                //Get Badge Away Team
                Call<Team> call3 = ScheduleApiService.service.getTeams(detailMatch[0].getIdAwayTeam());
                call3.enqueue(new Callback<Team>() {
                    @Override
                    public void onResponse(Call<Team> call3, Response<Team> response3) {
                        response3.body();
                        detailMatch[0].setUrlAwayBadge(response3.body().getTeams()[0].getStrTeamBadge());
                        Picasso.get().load(detailMatch[0].getUrlAwayBadge()).into(ivAwayBadgeTeam);
                    }

                    @Override
                    public void onFailure(Call<Team> call2, Throwable t2) {
                    }
                });
                //Get The Other
                tvLeagueEvent.setText(detailMatch[0].getStrLeague());
                tvDateMatch.setText(detailMatch[0].getStrDate());
                if(isMatched){
                    tvHomeScore.setText(detailMatch[0].getIntHomeScore());
                    tvAwayScore.setText(detailMatch[0].getIntAwayScore());
                } else {
                    tvHomeScore.setVisibility(View.INVISIBLE);
                    tvAwayScore.setVisibility(View.INVISIBLE);
                }
                tvHomeTeam.setText(detailMatch[0].getStrHomeTeam());
                tvAwayTeam.setText(detailMatch[0].getStrAwayTeam());
                if(isMatched){
                    if (detailMatch[0].getStrHomeGoalDetails() != null) {
                        homeGoals = detailMatch[0].getStrHomeGoalDetails().split(";",Integer.parseInt(detailMatch[0].getIntHomeScore())+1);
                        StringBuilder homeGoalsDetails = new StringBuilder();
                        for (int i=0; i < homeGoals.length; i++){
                            homeGoalsDetails.append(homeGoals[i]+"\n");
                        }
                        Log.d("HomeGoalsDetails",homeGoalsDetails.toString());
                        tvHomeGoals.setText(homeGoalsDetails.toString());
                    } else {
                        tvHomeGoals.setText("No Information");
                    }

                    if(detailMatch[0].getStrAwayGoalDetails() != null){
                        awayGoals = detailMatch[0].getStrAwayGoalDetails().split(",",Integer.parseInt(detailMatch[0].getIntAwayScore())+1);
                        StringBuilder awayGoalsDetails = new StringBuilder();
                        for (int i=0; i < awayGoals.length; i++){
                            awayGoalsDetails.append(awayGoals[i]+"\n");
                        }
                        tvAwayGoals.setText(awayGoalsDetails);
                    } else {
                        tvAwayGoals.setText("No Information");
                    }

                    if (detailMatch[0].getIntHomeShots() != null){
                        tvHomeShot.setText(detailMatch[0].getIntHomeShots());
                    } else {
                        tvHomeShot.setText("No Information");
                    }

                    if (detailMatch[0].getIntAwayShots() != null){
                        tvAwayShot.setText(detailMatch[0].getIntAwayShots());
                    } else {
                        tvAwayShot.setText("No Information");
                    }
                    ivWeatherInfo.setVisibility(GONE);
                    tvWeatherInfo.setVisibility(GONE);
                } else {
                    ivBall.setVisibility(GONE);
                    tvHomeGoals.setVisibility(GONE);
                    tvAwayGoals.setVisibility(GONE);
                    line2.setVisibility(GONE);
                    tvShotOnTarget.setVisibility(GONE);
                    tvHomeShot.setVisibility(GONE);
                    tvAwayShot.setVisibility(GONE);
                    tvFullTime.setText("UPCOMING MATCH");
                }


            }

            @Override
            public void onFailure(Call<DetailMatchEvents> call, Throwable t) {
                Log.d("error","onFailure TeamView Acticity");
            }
        });


    }

    public void awayTeamDetail(View view) {
        Intent AwayTeamDetail = new Intent(this,TeamViewActivity.class);
        AwayTeamDetail.putExtra("teamID",idAwayTeam);
        startActivity(AwayTeamDetail);
    }

    public void homeTeamDetail(View view) {
        Intent HomeTeamDetail = new Intent(this,TeamViewActivity.class);
        HomeTeamDetail.putExtra("teamID",idHomeTeam);
        startActivity(HomeTeamDetail);
    }

    public String StadiumLocation(String strHomeStadiumLocation) {
        String[] split = strHomeStadiumLocation.split(",");
        return split[1];
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
