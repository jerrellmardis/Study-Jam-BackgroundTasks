package com.jerrellmardis.studyjam.backgroundtasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity {

  public static final String UPDATE_TIMER_JOB_TAG = "updateTimerJob";

  private TextView timerTextView;
  private int currentTimer = 0;

  private BroadcastReceiver updateTimerReceiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      updateTimer(++currentTimer);
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // set up the content view
    setContentView(R.layout.activity_main);

    // get a reference to the TextView
    timerTextView = (TextView) findViewById(R.id.timer_text_view);

    // start the timer
    startTimer();

    // start the service
    //startService(new Intent(this, UpdateTimerService.class));

    // start the update timer job
    //scheduleTimerUpdate();

    // send the notification
    //NotificationUtils.sendNotification(this);
  }

  @Override protected void onResume() {
    super.onResume();

    // register the broadcast receiver
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("updateTimer");
    registerReceiver(updateTimerReceiver, intentFilter);
  }

  @Override protected void onPause() {
    super.onPause();

    // unregister the update timer broadcast receiver
    unregisterReceiver(updateTimerReceiver);
  }

  private void updateTimer(int currentTime) {
    timerTextView.setText(String.format(getString(R.string.timer_seconds), currentTime));
  }

  private void startTimer() {
    new Thread() {
      @Override public void run() {
        try {
          while (!isInterrupted()) {
            // update the timer every second
            Thread.sleep(1000);

            runOnUiThread(new Runnable() {
              @Override public void run() {
                updateTimer(++currentTimer);
              }
            });
          }
        } catch (InterruptedException ignored) {
          // do nothing
        }
      }
    }.start();
  }

  private void scheduleTimerUpdate() {
    Driver driver = new GooglePlayDriver(this);
    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
    Job job = dispatcher.newJobBuilder()
        .setService(UpdateTimerJobService.class)
        .setTag(UPDATE_TIMER_JOB_TAG)
        .setRecurring(true)
        .setTrigger(Trigger.executionWindow(1, 1))
        .build();
    dispatcher.schedule(job);
  }
}
