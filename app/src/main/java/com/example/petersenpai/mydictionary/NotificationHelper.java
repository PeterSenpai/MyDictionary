package com.example.petersenpai.mydictionary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;

public class NotificationHelper extends ContextWrapper {

    private static final String myChannel_id = "com.example.rimble.pdict";
    private static final String myChannel_name = "PZ Dictionary Channel";
    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels() {
        NotificationChannel myChannel = new NotificationChannel(myChannel_id, myChannel_name, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(myChannel);

    }

    public NotificationManager getManager() {
        if (manager == null){
            return (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public Notification.Builder getmyChannel(String title, String body){
        return new Notification.Builder(getApplicationContext(), myChannel_id)
                .setContentText(body)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true);

    }
}
