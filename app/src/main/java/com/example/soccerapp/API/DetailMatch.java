package com.example.soccerapp.API;

import com.squareup.moshi.Json;

public class DetailMatch {
    @Json(name="idEvent") String idEvent;
    @Json(name="strLeague") String strLeague;
    @Json(name="strHomeTeam") String strHomeTeam;
    @Json(name="strAwayTeam") String strAwayTeam;
    @Json(name="intHomeScore") String intHomeScore;
    @Json(name="intAwayScore") String intAwayScore;
    @Json(name="strHomeGoalDetails") String strHomeGoalDetails;
    @Json(name="strAwayGoalDetails") String strAwayGoalDetails;
    @Json(name="strDate") String strDate;
    @Json(name="intHomeShots") String intHomeShots;
    @Json(name="intAwayShots") String intAwayShots;
    @Json(name="idHomeTeam") String idHomeTeam;
    @Json(name="idAwayTeam") String idAwayTeam;
    @Json(name="strTweet2") String urlHomeBadge;
    @Json(name="strTweet3") String urlAwayBadge;
    @Json(name="strTime") String strTime;

    public DetailMatch(String idEvent, String strLeague, String strHomeTeam, String strAwayTeam, String intHomeScore,
                       String intAwayScore, String strHomeGoalDetails, String strAwayGoalDetails, String strDate, String intHomeShots,
                       String intAwayShots, String idHomeTeam, String idAwayTeam, String urlHomeBadge, String urlAwayBadge) {

        this.idEvent = idEvent;
        this.strLeague = strLeague;
        this.strHomeTeam = strHomeTeam;
        this.strAwayTeam = strAwayTeam;
        this.intHomeScore = intHomeScore;
        this.intAwayScore = intAwayScore;
        this.strHomeGoalDetails = strHomeGoalDetails;
        this.strAwayGoalDetails = strAwayGoalDetails;
        this.strDate = strDate;
        this.intHomeShots = intHomeShots;
        this.intAwayShots = intAwayShots;
        this.idHomeTeam = idHomeTeam;
        this.idAwayTeam = idAwayTeam;
        this.urlHomeBadge = urlHomeBadge;
        this.urlAwayBadge = urlAwayBadge;
    }

    public String getStrTime(){return this.strTime;}
    public String getIdEvent(){return this.idEvent;}
    public String getStrLeague(){return this.strLeague;}
    public String getStrHomeTeam(){return this.strHomeTeam;}
    public String getStrAwayTeam(){return this.strAwayTeam;}
    public String getIntHomeScore(){return this.intHomeScore;}
    public String getIntAwayScore(){return this.intAwayScore;}
    public String getStrHomeGoalDetails(){return this.strHomeGoalDetails;}
    public String getStrAwayGoalDetails(){return this.strAwayGoalDetails;}
    public String getStrDate(){return this.strDate;}
    public String getIntHomeShots(){return this.intHomeShots;}
    public String getIntAwayShots(){return this.intAwayShots;}
    public String getIdHomeTeam(){return this.idHomeTeam;}
    public String getIdAwayTeam(){return this.idAwayTeam;}
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
}
