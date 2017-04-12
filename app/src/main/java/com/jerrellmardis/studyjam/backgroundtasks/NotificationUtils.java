package com.jerrellmardis.studyjam.backgroundtasks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.content.ContextCompat;

class NotificationUtils {

  private static final int TIMER_NOTIFICATION_ID = 304;

  private static final int TIMER_PENDING_INTENT_ID = 1215;
  private static final int UPDATE_TIMER_PENDING_INTENT_ID = 1519;

  private static final String TIMER_NOTIFICATION_UPDATE_ACTION = "updateTimer";

  static void sendNotification(Context context) {
    // create an Intent to start
    Intent startActivityIntent = new Intent(context, MainActivity.class);

    // create the PendingIntent wrapper which allows the NotificationManager to launch an Activity
    // in this app as if it were sent from this app
    PendingIntent pendingIntent =
        PendingIntent.getActivity(context, TIMER_PENDING_INTENT_ID, startActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT);

    // build the notification
    NotificationCompat.Builder notificationBuilder =
        new NotificationCompat.Builder(context).setColor(
            ContextCompat.getColor(context, R.color.colorPrimary))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.update_timer))
            .setContentText(context.getString(R.string.use_button_to_update_timer))
            .setStyle(new NotificationCompat.BigTextStyle().bigText(
                context.getString(R.string.use_button_to_update_timer)))
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .addAction(updateTimerAction(context))
            .setAutoCancel(true);

    // set the notification priority
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
    }

    // get a reference to the NotificationManager
    NotificationManager notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    // send the notification
    notificationManager.notify(TIMER_NOTIFICATION_ID, notificationBuilder.build());
  }

  /**
   * Builds the notification action's PendingIntent.
   *
   * @param context the context
   * @return the Action
   */
  private static Action updateTimerAction(Context context) {
    Intent intent = new Intent(context, UpdateTimerService.class);
    intent.setAction(TIMER_NOTIFICATION_UPDATE_ACTION);
    PendingIntent pendingIntent =
        PendingIntent.getService(context, UPDATE_TIMER_PENDING_INTENT_ID, intent,
            PendingIntent.FLAG_CANCEL_CURRENT);
    int iconResId = 0;
    return new Action(iconResId, context.getString(R.string.update), pendingIntent);
  }
}
