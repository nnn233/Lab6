package com.example.lab6;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotificationDao {
    @Query("SELECT * FROM notifications")
    List<NotificationEntity> getAll();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(NotificationEntity... songs);

    @Query("DELETE FROM notifications WHERE id=:id")
    void deleteById(int id);
}
