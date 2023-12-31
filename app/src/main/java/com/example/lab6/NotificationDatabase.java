package com.example.lab6;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NotificationEntity.class}, version = 1,
        exportSchema = false)
public abstract class NotificationDatabase extends RoomDatabase {
    public abstract NotificationDao notificationDao();

    private static volatile NotificationDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static NotificationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NotificationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    NotificationDatabase.class, "notification_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
