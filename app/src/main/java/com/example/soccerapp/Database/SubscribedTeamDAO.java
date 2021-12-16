package com.example.soccerapp.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SubscribedTeamDAO {
    @Query("SELECT * FROM SubscribedTeam WHERE idTeam LIKE :idteam LIMIT 1")
    SubscribedTeam getTeam(String idteam);

    @Query("SELECT * FROM SubscribedTeam")
    List<SubscribedTeam> getAllTeam();

    @Query("SELECT COUNT(*) FROM SubscribedTeam")
    int getCount();

    @Insert
    void insertTeam(SubscribedTeam... subscribedteam);

    @Delete
    void delete(SubscribedTeam subscribedTeam);
    

}
