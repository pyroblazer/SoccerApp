package com.example.soccerapp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


import androidx.annotation.Nullable;

public class StepCountingService extends Service implements SensorEventListener {
    SensorManager sensorManager;
    Sensor stepCounterSensor;
    int stepCounter;
    int newStepCounter;
    public static final String BROADCAST_ACTION = "com.example.soccerapp";

    // Create a handler - that will be used to broadcast our data, after a specified amount of time.
    private final Handler handler = new Handler();
    Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();

        intent = new Intent(BROADCAST_ACTION);
        Log.v("Service", "Start");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Service", "Start");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, stepCounterSensor, 0);

        stepCounter = 0;
        newStepCounter = 0;

        // remove any existing callbacks to the handler
        handler.removeCallbacks(updateBroadcastData);
        // call our handler with or without delay.
        handler.post(updateBroadcastData); // 0 seconds

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable updateBroadcastData = new Runnable() {
        public void run() {
            broadcastSensorValue();
            // Call "handler.postDelayed" again, after a specified delay.
            handler.postDelayed(this, 1000);
        }
    };

    private void broadcastSensorValue() {
        // add step counter to intent.
        intent.putExtra("Counted_Step", String.valueOf(newStepCounter));
        // call sendBroadcast with that intent  - which sends a message to whoever is registered to receive it.
        sendBroadcast(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.v("senser", "change");
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int countSteps = (int) event.values[0];

            if (stepCounter == 0) { // If the stepCounter is in its initial value, then...
                stepCounter = (int) event.values[0]; // Assign the StepCounter Sensor event value to it.
            }
            newStepCounter = countSteps - stepCounter; // By subtracting the stepCounter variable from the Sensor event value - We start a new counting sequence from 0. Where the Sensor event value will increase, and stepCounter value will be only initialised once.
            Log.v("step counter", String.valueOf(newStepCounter));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
