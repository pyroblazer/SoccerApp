package com.example.soccerapp.API;

import com.squareup.moshi.Json;

public class DetailTeam {
    @Json(name="idTeam") String idTeam;
    @Json(name="strTeam") String strTeam;
    @Json(name="strTeamShort") String strTeamShort;
    @Json(name="strAlternate") String strAlternate;
    @Json(name="intFormedYear") String intFormedYear;
    @Json(name="strSport") String strSport;
    @Json(name="strLeague") String strLeague;
    @Json(name="strManager") String strManager;
    @Json(name="strStadium") String strStadium;
    @Json(name="strStadiumLocation") String strStadiumLocation;
    @Json(name="intStadiumCapacity") String intStadiumCapacity;
    @Json(name="strTeamBadge") String strTeamBadge;

    public DetailTeam(String idTeam, String strTeam, String strTeamShort, String strAlternate, String intFormedYear, String strSport,
                      String strLeague, String strManager, String strStadium, String strStadiumLocation, String intStadiumCapacity,
                      String strTeamBadge){
        this.idTeam = idTeam;
        this.strTeam = strTeam;
        this.strTeamShort = strTeamShort;
        this.strAlternate = strAlternate;
        this.intFormedYear = intFormedYear;
        this.strSport = strSport;
        this.strLeague = strLeague;
        this.strManager = strManager;
        this.strStadium = strStadium;
        this.strStadiumLocation = strStadiumLocation;
        this.intStadiumCapacity = intStadiumCapacity;
        this.strTeamBadge = strTeamBadge;
    }

    public String getIdTeam(){
        return this.idTeam;
    }

    public String getStrTeam(){
        return this.strTeam;
    }

    public String getStrTeamShort(){
        return this.strTeamShort;
    }

    public String getStrAlternate(){
        return this.strAlternate;
    }

    public String getIntFormedYear(){
        return this.intFormedYear;
    }

    public String getStrSport(){
        return this.strSport;
    }

    public String getStrLeague(){
        return this.strLeague;
    }

    public String getStrManager(){
        return this.strManager;
    }

    public String getStrStadium(){
        return this.strStadium;
    }

    public String getStrStadiumLocation(){
        return this.strStadiumLocation;
    }

    public String getIntStadiumCapacity(){
        return this.intStadiumCapacity;
    }

    public String getStrTeamBadge() {
        return strTeamBadge;
    }
}
