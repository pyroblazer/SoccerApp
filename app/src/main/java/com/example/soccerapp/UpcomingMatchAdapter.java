package com.example.soccerapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soccerapp.API.ScheduleData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpcomingMatchAdapter extends RecyclerView.Adapter<UpcomingMatchAdapter.MyViewHolder> {

    private List<ScheduleData> mDataset;
    private List<ScheduleData> mDatasetAll;

    public UpcomingMatchAdapter(List<ScheduleData> scheduleDataList){
        mDataset = scheduleDataList;
        mDatasetAll = new ArrayList<>(mDataset);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvHomeTeam;
        public TextView tvAwayTeam;
        public TextView tvDateEvent;
        public TextView tvLeagueEvent;
        public ImageView imageBadgeHomeTeam, imageBadgeAwayTeam;
        public LinearLayout mainlinear;
        public TextView tvStadium;


        public MyViewHolder(View v) {
            super(v);
            mainlinear = v.findViewById(R.id.upcomingmainliner);
            imageBadgeHomeTeam = v.findViewById(R.id.upcoming_badge_home_team);
            imageBadgeAwayTeam = v.findViewById(R.id.upcoming_badge_away_team);
            tvHomeTeam = v.findViewById(R.id.upcoming_name_home_team);
            tvAwayTeam = v.findViewById(R.id.upcoming_name_away_team);
            tvDateEvent = v.findViewById(R.id.upcoming_date_match_event);
            tvLeagueEvent = v.findViewById(R.id.upcoming_league_event);
            tvStadium = v.findViewById(R.id.stadium);
        }
    }

    @Override
    public UpcomingMatchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcoming_match_item, parent, false);
        UpcomingMatchAdapter.MyViewHolder vh = new UpcomingMatchAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final UpcomingMatchAdapter.MyViewHolder holder,final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tvLeagueEvent.setText(mDataset.get(position).getLeague());
        Picasso.get().load(mDataset.get(position).getUrlHomeBadge()).into(holder.imageBadgeHomeTeam);
        Picasso.get().load(mDataset.get(position).getUrlAwayBadge()).into(holder.imageBadgeAwayTeam);
        holder.tvHomeTeam.setText(mDataset.get(position).getHomeTeam());
        holder.tvAwayTeam.setText(mDataset.get(position).getAwayTeam());
        holder.tvDateEvent.setText(parseStrDate(mDataset.get(position).getDateEvent()));
        holder.tvStadium.setText(mDataset.get(position).getStrStadium());
        holder.mainlinear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("Main Linear","CLICKED");
                sendIntentMatchDetail(holder.tvHomeTeam.getContext(),mDataset.get(position).getIdEvent());
            }
        });

        holder.imageBadgeHomeTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Home Badge", "CLICKED Home");
                sendIntent(holder.imageBadgeHomeTeam.getContext(),mDataset.get(position).getIdHomeTeam());
            }
        });

        holder.imageBadgeAwayTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Away Badge", "CLICKED Away");
                sendIntent(holder.imageBadgeAwayTeam.getContext(),mDataset.get(position).getIdAwayTeam());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void sendIntentMatchDetail(Context context, String Data){
        Intent MatchDetail = new Intent(context,MatchDetail.class);
        MatchDetail.putExtra("eventID",Data);
        MatchDetail.putExtra("isMatched",false);
        context.startActivity(MatchDetail);
    }

    public void sendIntent(Context context, String Data){
        Intent HomeTeamDetail = new Intent(context,TeamViewActivity.class);
        HomeTeamDetail.putExtra("teamID",Data);
        context.startActivity(HomeTeamDetail);
    }

    public String parseStrDate(String strDate) {
        String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
                "November", "December"};
        String[] split = strDate.split("-");
        return split[2] + " " + month[Integer.parseInt(split[1])-1] + " " + split[0];
    }

}
