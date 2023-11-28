package com.example.lab6;

import android.app.Application;
import android.content.Context;

public class LabApplication extends Application {
    private NotificationRepository repository;

    private NotificationDatabase database;

    public LabApplication() {

    }

    public static LabApplication getInstance(Context context) {
        return (LabApplication) context.getApplicationContext();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        database = NotificationDatabase.getDatabase(this.getApplicationContext());
        repository = new NotificationRepository(database.notificationDao());
    }

    public NotificationRepository getRepository() {
        return repository;
    }
}