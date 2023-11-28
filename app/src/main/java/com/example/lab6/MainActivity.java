package com.example.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton button;
    RecyclerView recyclerView;
    NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "On Create invoked");
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.creating_btn);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateNotificationActivity.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new NotificationAdapter(MainActivity.this);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SwipeCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        LabApplication app = LabApplication.getInstance(this);

        app.getRepository().getItems().observe(this, items -> {
            if (items.size() != 0) {
                adapter.setNotifications((ArrayList<NotificationEntity>) items);
                Log.i("MainActivity", "items В базе данных " + items.size());
            }
        });
    }
}