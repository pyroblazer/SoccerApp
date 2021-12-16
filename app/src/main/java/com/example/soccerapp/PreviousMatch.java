package com.example.soccerapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soccerapp.API.Results;
import com.example.soccerapp.API.ScheduleApiService;
import com.example.soccerapp.API.ScheduleData;
import com.example.soccerapp.API.Team;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviousMatch extends Fragment implements RecyclerViewClickListener{
    private RecyclerView recyclerView;
    private PreviousMatchAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<ScheduleData> mDataset;
    List<String> urlHomeBadge;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.previous_match, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        recyclerView = (RecyclerView) view.findViewById(R.id.previous_match_list);
        mDataset = new ArrayList<>();
        urlHomeBadge = new ArrayList<>();
        String teamID = getArguments().getString("teamID");
        Call<Results> call = ScheduleApiService.service.getLastSchedule(teamID);
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                response.body();
                ScheduleData[] arraysEvents = response.body().getResults();
                for (int i=0; i<arraysEvents.length; i++) {
                    ScheduleData data = new ScheduleData(arraysEvents[i].getIdEvent(),arraysEvents[i].getIdHomeTeam(), arraysEvents[i].getIdAwayTeam(),
                            arraysEvents[i].getHomeTeam(),arraysEvents[i].getAwayTeam(),
                            arraysEvents[i].getHomeScore(), arraysEvents[i].getAwayScore(),
                            arraysEvents[i].getDateEvent(), "google.com",
                            "google.com",arraysEvents[i].getLeague(), "Emirates Stadium");
                    final int temp = i;
                    mDataset.add(data);
                    getHomeBadge(arraysEvents[i].getIdHomeTeam(),temp,mDataset);
                    getAwayBadge(arraysEvents[i].getIdAwayTeam(),temp,mDataset);
                }
                generateDataList(mDataset);

            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Log.d("error", "asd");
            }

        });
    }

    @Override
    public void onViewClicked(View v, int position) {
        if(v.getId() == R.id.badge_home_team){
            // Do your stuff here
            Toast.makeText(getContext(),"Home Badge Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRowClicked(int position) {
        // Clicked entire row
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
            }
        });
    }

    private void generateDataList(List<ScheduleData> scheduleDataList) {
        Log.d("Start APP : ", "START");
//        mDataset.add(new ScheduleData("Arsenal","Manchester United","Arsenal","Manchester United","10","2","21-02-2020","https://www.thesportsdb.com/images/media/team/badge/a1af2i1557005128.png","https://www.thesportsdb.com/images/media/team/badge/a1af2i1557005128.png","Premiere League"));
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new PreviousMatchAdapter(scheduleDataList);
        recyclerView.setAdapter(mAdapter);
        Log.d("RecyclerView", "Go in the recyclerview");
    }
}
