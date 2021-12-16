package com.example.soccerapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Schedule.class}, version = 1, exportSchema = false)
public abstract class ScheduleDatabase extends RoomDatabase {
    public abstract ScheduleDAO scheduleDao();
}
