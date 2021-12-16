package com.example.soccerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soccerapp.API.DetailTeam;
import com.example.soccerapp.API.ScheduleApiService;
import com.example.soccerapp.API.Team;
import com.example.soccerapp.Database.SubscribedTeam;
import com.example.soccerapp.Database.SubscribedTeamRepository;
import com.example.soccerapp.Notification.NotificationActivity;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamViewActivity extends AppCompatActivity {
    private ImageView badge_team;
    private TextView strTeamShort;
    private TextView strFormedYear;
    private TextView strLeague;
    private TextView strManager;
    private TextView strStadium;
    private TextView strStadiumLocation;
    private TextView strStadiumCapacity;
    private TextView strTeamName;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button btnSubs;
    private Boolean isSubscribe;
    private String teamID;
    private SubscribedTeamRepository subsTeamRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        badge_team = (ImageView) findViewById(R.id.badge_team);
        strTeamShort = (TextView) findViewById(R.id.strTeamShort);
        strFormedYear = (TextView) findViewById(R.id.strFormedYear);
        strLeague = (TextView) findViewById(R.id.strLeague);
        strManager = (TextView) findViewById(R.id.strManager);
        strStadium = (TextView) findViewById(R.id.strStadium);
        strStadiumCapacity = (TextView) findViewById(R.id.strStadiumCapacity);
        strStadiumLocation = (TextView) findViewById(R.id.strStadiumLocation);
        strTeamName = (TextView) findViewById(R.id.team_name);
        btnSubs = (Button) findViewById(R.id.subscribe);

        Intent getTeamID = getIntent();
        teamID = (String) getTeamID.getExtras().get("teamID");
        Log.d("Team ID",teamID);
        //Database
        subsTeamRepo = new SubscribedTeamRepository(getApplicationContext());
        new Thread(new Runnable(){
            public void run(){
                if(subsTeamRepo.checkExistTeam(teamID)){
                    isSubscribe = true;
                    btnSubs.setText("UNSUBSCRIBE");
                } else {
                    isSubscribe = false;
                    btnSubs.setText("SUBSCRIBE");
                }
            }
        }).start();
        Call<Team> call = ScheduleApiService.service.getTeams(teamID);
        call.enqueue(new Callback<Team>(){

            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {
                response.body();
                DetailTeam[] detailTeam = response.body().getTeams();
                Picasso.get().load(detailTeam[0].getStrTeamBadge()).into(badge_team);
                strTeamName.setText(detailTeam[0].getStrTeam());
                strTeamShort.setText(detailTeam[0].getStrAlternate());
                strFormedYear.setText(detailTeam[0].getIntFormedYear());
                strLeague.setText(detailTeam[0].getStrLeague());
                strManager.setText(detailTeam[0].getStrManager());
                strStadium.setText(detailTeam[0].getStrStadium());
                strStadiumCapacity.setText(detailTeam[0].getIntStadiumCapacity());
                strStadiumLocation.setText(detailTeam[0].getStrStadiumLocation());
            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
                Log.d("error","onFailure TeamView Acticity");
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        adapter = new TabAdapter(getSupportFragmentManager());
        Fragment previousMatchFragment = new PreviousMatch();
        Fragment upcomingMatchFragment = new UpcomingMatch();
        Bundle teamIDBundle = new Bundle();
        teamIDBundle.putString("teamID",teamID);
        previousMatchFragment.setArguments(teamIDBundle);
        upcomingMatchFragment.setArguments(teamIDBundle);
        adapter.addFragment(previousMatchFragment, "Previous Match");
        adapter.addFragment(upcomingMatchFragment, "Upcoming Match");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void subscribeTeam(View view) {
        SubscribedTeam team = new SubscribedTeam(teamID,strTeamName.getText().toString());
        if (!isSubscribe){
            subsTeamRepo.insertTeam(team);
            isSubscribe = true;
            btnSubs.setText("UNSUBSCRIBE");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"Channel1")
                    .setSmallIcon(R.drawable.playericon)
                    .setContentTitle("Congratulations")
                    .setContentText("You Just Subscribed " + team.getNameTeam() + " as Your Favorite Team")
                    .setAutoCancel(true);

            Log.d("Notif", "At builder");
            Intent intent = new Intent(TeamViewActivity.this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Message","You Just Subscribed " + team.getNameTeam() + " as Your Favorite Team" );

            PendingIntent pendingIntent = PendingIntent.getActivity(TeamViewActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Channel1",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(0,builder.build());
        } else {
            subsTeamRepo.deleteTeam(team);
            isSubscribe = false;
            btnSubs.setText("SUBSCRIBE");
        }

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
