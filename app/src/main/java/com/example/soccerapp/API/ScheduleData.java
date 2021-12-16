package com.example.soccerapp.API;

import com.squareup.moshi.Json;

public class ScheduleData {
    @Json(name="idEvent") String idEvent;
    @Json(name="idHomeTeam") String idHomeTeam;
    @Json(name="idAwayTeam") String idAwayTeam;
    @Json(name="strHomeTeam") String homeTeam;
    @Json(name="strAwayTeam") String awayTeam;
    @Json(name="intHomeScore") String homeScore;
    @Json(name="intAwayScore") String awayScore;
    @Json(name="dateEvent") String dateEvent;
    @Json(name="strTweet2") String urlHomeBadge;
    @Json(name="strTweet3") String urlAwayBadge;
    @Json(name="strLeague") String strLeague;
    @Json(name="strCity") String strStadium;

    public ScheduleData(String idEvent,String idHomeTeam, String idAwayTeam,
                        String homeTeam, String awayTeam,
                        String homeScore, String awayScore,
                        String dateEvent, String urlHomeBadge, String urlAwayBadge,
                        String strLeague, String strStadium) {
        this.idEvent = idEvent;
        this.idHomeTeam = idHomeTeam;
        this.idAwayTeam = idAwayTeam;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.dateEvent = dateEvent;
        this.urlHomeBadge = urlHomeBadge;
        this.urlAwayBadge = urlAwayBadge;
        this.strLeague = strLeague;
        this.strStadium = strStadium;
    }

    public String getIdEvent() { return this.idEvent; }

    public String getLeague() { return this.strLeague; }

    public String getAwayScore() {
        return awayScore;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public String getIdHomeTeam() {
        return idHomeTeam;
    }

    public String getIdAwayTeam() {
        return idAwayTeam;
    }

    public String getUrlHomeBadge() {
        return urlHomeBadge;
    }

    public void setUrlHomeBadge(String urlHomeBadge) {
        this.urlHomeBadge = urlHomeBadge;
    }

    public String getUrlAwayBadge() {
        return urlAwayBadge;
    }

    public void setUrlAwayBadge(String urlAwayBadge) {
        this.urlAwayBadge = urlAwayBadge;
    }

    public String getStrStadium() {
        return strStadium;
    }

    public void setStrStadium(String strStadium) {
        this.strStadium = strStadium;
    }
}
