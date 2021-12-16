package com.example.soccerapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soccerapp.API.ScheduleData;
import com.example.soccerapp.Database.Schedule;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> implements Filterable {
    private List<Schedule> mDataset;
    private List<Schedule> mDatasetAll;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ScheduleAdapter(List<Schedule> scheduleDataList) {
        mDataset = scheduleDataList;
        mDatasetAll = new ArrayList<>(mDataset);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Schedule> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(mDatasetAll);
            } else {
                for (Schedule obj: mDatasetAll) {
                    if (obj.getHomeTeam().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            obj.getAwayTeam().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(obj);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mDataset.clear();
            mDataset.addAll((Collection<? extends Schedule>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvHomeTeam;
        public TextView tvAwayTeam;
        public TextView tvScoreHomeTeam;
        public TextView tvScoreAwayTeam;
        public TextView tvDateEvent;
        public TextView tvLeagueEvent;
        public ImageView ivBadgeHomeTeam, ivBadgeAwayTeam;
        public LinearLayout mainlinear;
        public TextView tvStadium;

        public MyViewHolder(View v) {
            super(v);
            mainlinear = v.findViewById(R.id.mainlinearlayout);
            tvHomeTeam = v.findViewById(R.id.home_team);
            tvAwayTeam = v.findViewById(R.id.away_team);
            tvScoreHomeTeam = v.findViewById(R.id.score_home_team);
            tvScoreAwayTeam = v.findViewById(R.id.score_away_team);
            tvDateEvent = v.findViewById(R.id.date_event);
            ivBadgeHomeTeam = v.findViewById(R.id.badge_team);
            ivBadgeAwayTeam = v.findViewById(R.id.badge_away_team);
            tvLeagueEvent = v.findViewById(R.id.league_event);
            tvStadium = v.findViewById(R.id.stadium);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ScheduleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tvHomeTeam.setText(mDataset.get(position).getHomeTeam());
        holder.tvAwayTeam.setText(mDataset.get(position).getAwayTeam());
        holder.tvScoreHomeTeam.setText(mDataset.get(position).getHomeScore());
        holder.tvScoreAwayTeam.setText(mDataset.get(position).getAwayScore());
        holder.tvDateEvent.setText(parseStrDate(mDataset.get(position).getDateEvent()));
        holder.tvLeagueEvent.setText(mDataset.get(position).getLeague());
        holder.tvStadium.setText(mDataset.get(position).getStrStadium());
        Picasso.get().load(mDataset.get(position).getUrlHomeBadge()).into(holder.ivBadgeHomeTeam);
        Picasso.get().load(mDataset.get(position).getUrlAwayBadge()).into(holder.ivBadgeAwayTeam);

        holder.mainlinear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (isConnectingToInternet(holder.mainlinear.getContext())) {
                    Log.d("Main Linear","CLICKED");
                    sendIntentMatchDetail(holder.ivBadgeHomeTeam.getContext(),mDataset.get(position).getIdEvent());
                } else {
                    Toast.makeText(holder.mainlinear.getContext(), "No Connection Internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.ivBadgeHomeTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectingToInternet(holder.ivBadgeHomeTeam.getContext())) {
                    Log.d("Home Badge", "CLICKED Home");
                    sendIntentTeamDetails(holder.ivBadgeHomeTeam.getContext(),mDataset.get(position).getIdHomeTeam());
                } else {
                    Toast.makeText(holder.ivBadgeHomeTeam.getContext(), "No Connection Internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.ivBadgeAwayTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectingToInternet(holder.ivBadgeAwayTeam.getContext())) {
                    Log.d("Away Badge", "CLICKED Away");
                    sendIntentTeamDetails(holder.ivBadgeAwayTeam.getContext(),mDataset.get(position).getIdAwayTeam());
                } else {
                    Toast.makeText(holder.ivBadgeAwayTeam.getContext(), "No Connection Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sendIntentMatchDetail(Context context, String Data){
        Intent MatchDetail = new Intent(context,MatchDetail.class);
        MatchDetail.putExtra("eventID",Data);
        MatchDetail.putExtra("isMatched",true);
        context.startActivity(MatchDetail);
    }

    public void sendIntentTeamDetails(Context context, String Data){
        Intent HomeTeamDetail = new Intent(context,TeamViewActivity.class);
        HomeTeamDetail.putExtra("teamID",Data);
        context.startActivity(HomeTeamDetail);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public String parseStrDate(String strDate) {
        String[] month = {"Januari", "Febuari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober",
                "November", "Desember"};
        String[] split = strDate.split("-");
        return split[2] + " " + month[Integer.parseInt(split[1])-1] + " " + split[0];
    }

    public static boolean isConnectingToInternet(Context context)
    {
        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }
}
