package com.example.soccerapp.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class SubscribedTeam implements Serializable{

    @PrimaryKey
    @NonNull
    private String idTeam;

    @ColumnInfo(name="nameTeam")
    private String nameTeam;

    public SubscribedTeam(String idTeam, String nameTeam){
        this.idTeam = idTeam;
        this.nameTeam = nameTeam;
    }

    public String getIdTeam(){
        return this.idTeam;
    }

    public void setIdTeam(String idTeam){
        this.idTeam = idTeam;
    }

    public String getNameTeam(){
        return this.nameTeam;
    }

    public void setNameTeam(String nameTeam){
        this.nameTeam = nameTeam;
    }

    public String getString(){
        return "teamID = " + this.idTeam + " | teamName = " + this.nameTeam;
    }
}
