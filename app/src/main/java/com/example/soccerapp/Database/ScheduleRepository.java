package com.example.soccerapp.Database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

public class ScheduleRepository {
    private String DB_NAME = "schedule";
    private ScheduleDatabase scheduleDatabase;
    private List<Schedule> allSchedule;

    public ScheduleRepository(Context context) {
        scheduleDatabase = Room.databaseBuilder(context, ScheduleDatabase.class, DB_NAME).build();
    }

    public void insertSchedule(final Schedule schedule){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                scheduleDatabase.scheduleDao().insertSchedule(schedule);
                return null;
            }
        }.execute();
    }

    public void insertSchedules(final List<Schedule> schedules){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (Schedule obj : schedules) {
                    scheduleDatabase.scheduleDao().insertSchedule(obj);
                }
                return null;
            }
        }.execute();
    }

    public List<Schedule> getAllSchedule() throws ExecutionException, InterruptedException {
        return new GetScheduleAsyncTask().execute().get();
    }

    private class GetScheduleAsyncTask extends AsyncTask<Void, Void, List<Schedule>>
    {
        @Override
        protected List<Schedule> doInBackground(Void... url) {
            return scheduleDatabase.scheduleDao().loadAllSchedule();
        }
    }

}
