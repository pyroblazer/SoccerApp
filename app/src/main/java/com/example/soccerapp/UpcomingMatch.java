package com.example.soccerapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soccerapp.API.Events;
import com.example.soccerapp.API.Results;
import com.example.soccerapp.API.ScheduleApiService;
import com.example.soccerapp.API.ScheduleData;
import com.example.soccerapp.API.Team;
import com.example.soccerapp.Database.Schedule;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingMatch extends Fragment {
    private RecyclerView recyclerView2;
    private UpcomingMatchAdapter mAdapter2;
    private RecyclerView.LayoutManager layoutManager;
    List<ScheduleData> mDataset2;
    List<String> urlHomeBadge2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.upcoming_match, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        Log.d("STARTUPCOMING","Masuk");
        recyclerView2 = (RecyclerView) view.findViewById(R.id.upcoming_match_list);
        mDataset2 = new ArrayList<>();
        urlHomeBadge2 = new ArrayList<>();
        String teamID = getArguments().getString("teamID");
        Call<Events> call = ScheduleApiService.service.getUpcomingSchedule(teamID);
        call.enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                response.body();
                ScheduleData[] arraysEvents2 = response.body().getEvents();
                for (int i=0; i<arraysEvents2.length; i++) {
                    ScheduleData data = new ScheduleData(arraysEvents2[i].getIdEvent(),arraysEvents2[i].getIdHomeTeam(), arraysEvents2[i].getIdAwayTeam(),
                            arraysEvents2[i].getHomeTeam(),arraysEvents2[i].getAwayTeam(),
                            arraysEvents2[i].getHomeScore(), arraysEvents2[i].getAwayScore(),
                            arraysEvents2[i].getDateEvent(), "google.com",
                            "google.com",arraysEvents2[i].getLeague(), "Emirates Stadium");
                    final int temp = i;
                    mDataset2.add(data);
                    getHomeBadge(arraysEvents2[i].getIdHomeTeam(),temp,mDataset2);
                    getAwayBadge(arraysEvents2[i].getIdAwayTeam(),temp,mDataset2);
                }
            }

            @Override
            public void onFailure(Call<Events> call, Throwable t) {

            }
        });
    }

    public void getHomeBadge(String idTeam, final int i, final List<ScheduleData> mDataset) {
        Call<Team> call4 = ScheduleApiService.service.getTeams(idTeam);
        call4.enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call4, Response<Team> response4) {
                response4.body();
                mDataset.get(i).setUrlHomeBadge(response4.body().getTeams()[0].getStrTeamBadge());
                mDataset.get(i).setStrStadium(response4.body().getTeams()[0].getStrStadium());
                generateDataList(mDataset);
            }

            @Override
            public void onFailure(Call<Team> call2, Throwable t2) {
                Log.d("FailurHomeBadge :", "Failure");
            }
        });
    }

    public void getAwayBadge(String idTeam, final int i, final List<ScheduleData> mDataset) {
        Call<Team> call5 = ScheduleApiService.service.getTeams(idTeam);
        call5.enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call5, Response<Team> response5) {
                response5.body();
                mDataset.get(i).setUrlAwayBadge(response5.body().getTeams()[0].getStrTeamBadge());
                generateDataList(mDataset);
            }

            @Override
            public void onFailure(Call<Team> call2, Throwable t2) {
                Log.d("FailurHomeBadge :", "Failure");
            }
        });
    }

    private void generateDataList(List<ScheduleData> scheduleDataList) {
        Log.d("Start APP : ", "START");
//        mDataset.add(new ScheduleData("Arsenal","Manchester United","Arsenal","Manchester United","10","2","21-02-2020","https://www.thesportsdb.com/images/media/team/badge/a1af2i1557005128.png","https://www.thesportsdb.com/images/media/team/badge/a1af2i1557005128.png","Premiere League"));
        recyclerView2.setHasFixedSize(true);
//        if(getResources().getDisplayMetrics().widthPixels > getResources().getDisplayMetrics().heightPixels){
//            layoutManager = new GridLayoutManager(this.getContext(),2);
//        } else {
//            layoutManager = new GridLayoutManager(getContext(),1);
//        }
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView2.setLayoutManager(layoutManager);
        mAdapter2 = new UpcomingMatchAdapter(scheduleDataList);
        recyclerView2.setAdapter(mAdapter2);
        Log.d("RecyclerView", "Masuk ReccylerView");
    }
}
