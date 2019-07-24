package com.pdfmaker.erum.android.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pdfmaker.erum.android.Activities.AboutUs;
import com.pdfmaker.erum.android.Methods.KeyStrings;
import com.pdfmaker.erum.android.R;

import java.util.Objects;

public class FirebaseService extends FirebaseMessagingService {

    private static final String NOTIFICATION_CHANNEL_ID = "ChannelId";
    private static final String NOTIFICATION_CHANNEL_NAME = "ChannelName";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "ChannelDescription";
    private static final int NOTIFICATION_ID = 1;

    private String title = "";
    private String  message = "";
    private String url = "";
    private Intent intent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        message = remoteMessage.getNotification().getBody();
        url = remoteMessage.getData().get(KeyStrings.WEB_URL);

        sendNotification();

    }

    // Notification
    public void sendNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);

            notificationChannel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this,AboutUs.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        //Get an instance of NotificationManager
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setColor(Color.GREEN)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        // Showing Notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}
