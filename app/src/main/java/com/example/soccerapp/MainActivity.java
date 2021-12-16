package com.example.soccerapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.soccerapp.API.Events;
import com.example.soccerapp.API.ScheduleApiService;
import com.example.soccerapp.API.ScheduleData;
import com.example.soccerapp.API.Team;
import com.example.soccerapp.Database.Schedule;
import com.example.soccerapp.Database.ScheduleDAO;
import com.example.soccerapp.Database.ScheduleDatabase;
import com.example.soccerapp.Database.ScheduleRepository;
import com.example.soccerapp.Notification.NotificationWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String BROADCAST_ACTION = "com.example.soccerapp";
    private RecyclerView recyclerView;
    private ScheduleAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Schedule> mDataset;
    List<String> urlHomeBadge;
    private Intent intentCountService;
    String countedStep;
    TextView stepCountTV;
    ScheduleDAO scheduleDAO;
    ScheduleDatabase scheduleDatabase;
    ScheduleRepository scheduleRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scheduleRepository = new ScheduleRepository(getApplicationContext());
        stepCountTV = (TextView)findViewById(R.id.step_count_text);
//        startService(new Intent(this, StepCountingService.class));
        intentCountService = new Intent(this, StepCountingService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intentCountService, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),24*60*1000, pintent);
//        startService(new Intent(getBaseContext(), StepCountingService.class));
        registerReceiver(broadcastReceiver, new IntentFilter(StepCountingService.BROADCAST_ACTION));

        //Start Worker Thread for SubsTeam
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        PeriodicWorkRequest NotifWorker = new PeriodicWorkRequest.Builder(NotificationWorker.class,10, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(NotifWorker);

        urlHomeBadge = new ArrayList<>();
        mDataset = new ArrayList<>();


        if (isInternetAvailable()) {
            Call<Events> call = ScheduleApiService.service.getSchedule();
            call.enqueue(new Callback<Events>() {
                @Override
                public void onResponse(Call<Events> call, Response<Events> response) {
                    response.body();
                    final ScheduleData[] arraysEvents = response.body().getEvents();
                    for (int i=0; i<arraysEvents.length; i++) {
                        Schedule data = new Schedule(arraysEvents[i].getIdEvent(),arraysEvents[i].getIdHomeTeam(), arraysEvents[i].getIdAwayTeam(),
                                arraysEvents[i].getHomeTeam(),arraysEvents[i].getAwayTeam(),
                                arraysEvents[i].getHomeScore(), arraysEvents[i].getAwayScore(),
                                arraysEvents[i].getDateEvent(), "google.com",
                                "google.com",arraysEvents[i].getLeague(), "Emirates Stadium");
                        final int temp = i;
                        mDataset.add(data);
                        Call<Team> call2 = ScheduleApiService.service.getTeams(arraysEvents[i].getIdHomeTeam());
                        call2.enqueue(new Callback<Team>() {
                            @Override
                            public void onResponse(Call<Team> call2, Response<Team> response2) {
                                response2.body();
                                mDataset.get(temp).setUrlHomeBadge(response2.body().getTeams()[0].getStrTeamBadge());
                                mDataset.get(temp).setStrStadium(response2.body().getTeams()[0].getStrStadium());
                                generateDataList(mDataset);
                            }

                            @Override
                            public void onFailure(Call<Team> call2, Throwable t2) {
                            }
                        });

                        Call<Team> call3 = ScheduleApiService.service.getTeams(arraysEvents[i].getIdAwayTeam());
                        call3.enqueue(new Callback<Team>() {
                            @Override
                            public void onResponse(Call<Team> call3, Response<Team> response3) {
                                response3.body();
                                mDataset.get(temp).setUrlAwayBadge(response3.body().getTeams()[0].getStrTeamBadge());
                                generateDataList(mDataset);
                            }

                            @Override
                            public void onFailure(Call<Team> call3, Throwable t3) {

                            }
                        });
                    }
                    generateDataList(mDataset);
                }

                @Override
                public void onFailure(Call<Events> call, Throwable t) {
                    Log.d("error", t.getMessage());
                }
            });
        } else {
            try {
                mDataset = scheduleRepository.getAllSchedule();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            generateDataList(mDataset);
        }
    }

    public boolean isInternetAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data;
            // call updateUI passing in our intent which is holding the data to display.

            data = intent.getStringExtra("Counted_Step");
            updateViews(data);
        }
    };

    private void updateViews(String data) {
        stepCountTV.setText(data + " steps");
    }

    private void generateDataList(List<Schedule> scheduleDataList) {
        scheduleRepository.insertSchedules(scheduleDataList);
        recyclerView = (RecyclerView) findViewById(R.id.schedule_match);

        recyclerView.setHasFixedSize(true);
        /*landspace*/
        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                heightPixels)
        {
            layoutManager = new GridLayoutManager(this,2);
        }
        else
        {
            layoutManager = new GridLayoutManager(this,1);
        }
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ScheduleAdapter(scheduleDataList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("stepCount",stepCountTV.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
