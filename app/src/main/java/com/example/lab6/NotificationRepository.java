package com.example.lab6;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {
    private final NotificationDao localDao;

    public NotificationRepository(NotificationDao localDao) {
        this.localDao = localDao;
        _items = new MutableLiveData<>(new ArrayList<>());
        refreshItems();
    }

    private final MutableLiveData<List<NotificationEntity>> _items;

    public LiveData<List<NotificationEntity>> getItems() {
        return _items;
    }

    public void refreshItems() {
        Log.i("Repository", "Refresh function invoked");
        NotificationDatabase.databaseWriteExecutor.execute(() -> {
            List<NotificationEntity> list = localDao.getAll();
            Log.i("Repository", "list В базе данных " + list.size());
            _items.postValue(list);
        });

        Log.i("Repository", "_items В базе данных "+_items.getValue().size());
    }

    public void add(NotificationEntity notification) {
        NotificationDatabase.databaseWriteExecutor.execute(() -> localDao.insertAll(notification));
        refreshItems();
    }

    public void delete(int id) {
        NotificationDatabase.databaseWriteExecutor.execute(() -> localDao.deleteById(id));
        refreshItems();
    }
}
