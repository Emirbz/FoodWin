package com.esprit.foodwin.utility;


import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.esprit.foodwin.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
  @Override
  public void onMessageReceived(RemoteMessage message) {
    super.onMessageReceived(message);

    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(message.getNotification().getTitle())
            .setContentText(message.getNotification().getBody())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(new NotificationCompat.BigTextStyle())
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_focused)
            .setAutoCancel(true);

    NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0, notificationBuilder.build());
  }

}
