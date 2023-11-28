package com.example.lab6;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    String title, content;
    int id;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            id=intent.getExtras().getInt("id");
            title = intent.getExtras().getString("title");
            content = intent.getExtras().getString("content");
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Intent notificationIntent = new Intent(context, CreateNotificationActivity.class);
        notificationIntent.putExtra("title", title);
        notificationIntent.putExtra("description", content);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    100);
            return;
        }
        notificationManager.notify(id, nBuilder.build());
    }
}
