package com.example.soccerapp.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScheduleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSchedule(Schedule schedule);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSchedules(List<Schedule> schedules);

    @Query("SELECT * FROM schedule")
    public List<Schedule> loadAllSchedule();
}
