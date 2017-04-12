package com.jerrellmardis.studyjam.backgroundtasks;

import android.app.IntentService;
import android.content.Intent;

public class UpdateTimerService extends IntentService {

  public UpdateTimerService() {
    super("UpdateTimerService");
  }

  @Override protected void onHandleIntent(Intent intent) {
    // send a broadcast to update the timer TextView in the MainActivity
    sendBroadcast(new Intent("updateTimer"));
  }
}
