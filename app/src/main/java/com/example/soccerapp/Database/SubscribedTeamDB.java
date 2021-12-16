package com.example.soccerapp.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SubscribedTeam.class}, version = 1, exportSchema = false)
public abstract class SubscribedTeamDB extends RoomDatabase {
    public abstract SubscribedTeamDAO subscribedTeamDAO();
}
