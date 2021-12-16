package com.example.soccerapp.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import java.util.List;

public class SubscribedTeamRepository {
    private String DB_NAME = "subcribedTeam";

    private SubscribedTeamDB subsTeamDB;

    public SubscribedTeamRepository(Context context){
        subsTeamDB = Room.databaseBuilder(context,SubscribedTeamDB.class,DB_NAME).build();
    }

    public void insertTeam(String idTeam, String nameTeam){
        SubscribedTeam team = new SubscribedTeam(idTeam,nameTeam);
        insertTeam(team);
    }

    public void insertTeam(final SubscribedTeam team){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                subsTeamDB.subscribedTeamDAO().insertTeam(team);
                return null;
            }
        }.execute();
    }

    public boolean checkExistTeam(String idTeam){
        SubscribedTeam team = subsTeamDB.subscribedTeamDAO().getTeam(idTeam);
        if (team == null){
            return false;
        } else {
            return true;
        }
    }

    public void deleteTeam(final SubscribedTeam team){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                subsTeamDB.subscribedTeamDAO().delete(team);
                return null;
            }
        }.execute();
    }

    public int countTeam(){
        return subsTeamDB.subscribedTeamDAO().getCount();

    }

    public List<SubscribedTeam> getAllTeam(){
        return subsTeamDB.subscribedTeamDAO().getAllTeam();
    }


}
