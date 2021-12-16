package com.example.soccerapp.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.soccerapp.API.Events;
import com.example.soccerapp.API.ScheduleApiService;
import com.example.soccerapp.API.ScheduleData;
import com.example.soccerapp.Database.SubscribedTeam;
import com.example.soccerapp.Database.SubscribedTeamRepository;
import com.example.soccerapp.MatchDetail;
import com.example.soccerapp.R;
import com.example.soccerapp.TeamViewActivity;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationWorker extends Worker {
    private SubscribedTeamRepository subsTeamRepo;
    private List<SubscribedTeam> substeams;
    private Context context;
    private DateTime dateTimeNow;
    private Date dateNow;
    private DateTimeComparator dateTimeComparator;
    private int notifchannel;


    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params){
        super(context,params);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        notifchannel = 0;
        dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        subsTeamRepo = new SubscribedTeamRepository(getApplicationContext());
        substeams = subsTeamRepo.getAllTeam();
        dateTimeNow = new DateTime();
        dateNow = dateTimeNow.plusDays(1).toDate();


        if(substeams.size() != 0){
            for (int i = 0; i < substeams.size(); i++){
                Call<Events> call = ScheduleApiService.service.getUpcomingSchedule(substeams.get(i).getIdTeam());
                call.enqueue(new Callback<Events>() {

                    @Override
                    public void onResponse(Call<Events> call, Response<Events> response) {
                        response.body();
                        ScheduleData[] arraysEvents = response.body().getEvents();
                        for (int j = 0; j < arraysEvents.length; j++) {
                            Date dateMatch = null;
                            try {
                                dateMatch = new SimpleDateFormat("yyyy-MM-dd").parse(arraysEvents[j].getDateEvent());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            int retVal = dateTimeComparator.compare(dateNow,dateMatch);
                            Log.d("retVal",String.valueOf(retVal));
                            if (retVal == 0){
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context,String.valueOf(j))
                                    .setSmallIcon(R.drawable.playericon)
                                    .setContentTitle(arraysEvents[j].getHomeTeam() + " vs " + arraysEvents[j].getAwayTeam() + " Start Tomorrow")
                                    .setContentText(arraysEvents[j].getHomeTeam() +" Will Play Againts " + arraysEvents[j].getAwayTeam() + " Tomorrow! Don't Miss It!")
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(arraysEvents[j].getHomeTeam() +", Will Play Againts " + arraysEvents[j].getAwayTeam() + " Today! Don't Miss It!"))
                                    .setAutoCancel(true);

                                Intent intent = new Intent(context, MatchDetail.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("eventID",arraysEvents[j].getIdEvent() );
                                intent.putExtra("isMatched",false);
                                PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                builder.setContentIntent(pendingIntent);

                                CharSequence name = context.getString(R.string.channel_name);
                                String description = context.getString(R.string.channel_description);
                                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                NotificationChannel channel = new NotificationChannel(String.valueOf(notifchannel),name,importance);
                                channel.setDescription(description);
                                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                                notificationManager.createNotificationChannel(channel);
                                notificationManager.notify(notifchannel,builder.build());
                                notifchannel++;
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Events> call, Throwable t) {

                    }
                });
            }

        } else {
            Log.d("Worker List Team" ,"Null");
        }


        return Result.success();
    }
}
