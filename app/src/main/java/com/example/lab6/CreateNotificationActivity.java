package com.example.lab6;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class CreateNotificationActivity extends AppCompatActivity {
    ImageView close, save;
    EditText title, description;

    Button buttonDate, buttonTime;
    TextView dateText, timeText;

    Calendar dateAndTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_notification);

        title = findViewById(R.id.title_notification);
        description = findViewById(R.id.description);

        Bundle extras=getIntent().getExtras();
        if ( extras != null) {
            title.setText(extras.getString("title"));
            description.setText(extras.getString("description"));
        }

        close = findViewById(R.id.cancel);

        close.setOnClickListener(l -> this.getOnBackPressedDispatcher().onBackPressed());

        buttonDate = findViewById(R.id.set_date);
        buttonDate.setOnClickListener(l -> setDate());

        buttonTime = findViewById(R.id.set_time);
        buttonTime.setOnClickListener(l -> setTime());

        dateText = findViewById(R.id.date_text);
        timeText = findViewById(R.id.time_text);

        save = findViewById(R.id.save);
        save.setOnClickListener(l -> {
            if (!dateText.getText().equals("") && !timeText.getText().equals("")) {
                NotificationEntity entity = new NotificationEntity(0, title.getText().toString(), description.getText().toString(), dateAndTime.getTimeInMillis());
                LabApplication.getInstance(this).getRepository().add(entity);
                setAlarm(entity);
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
                } else {
                    notificationChannel();
                }
                this.getOnBackPressedDispatcher().onBackPressed();
            } else Toast.makeText(this, "Укажите дату и время", Toast.LENGTH_SHORT).show();
        });
    }

    public void setDate() {
        new DatePickerDialog(this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void setTime() {
        new TimePickerDialog(this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    TimePickerDialog.OnTimeSetListener t = (view, hourOfDay, minute) -> {
        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dateAndTime.set(Calendar.MINUTE, minute);
        timeText.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME));

    };

    DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
        dateAndTime.set(Calendar.YEAR, year);
        dateAndTime.set(Calendar.MONTH, monthOfYear);
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateText.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    };

    private void notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(" CHANNEL_DESCRIPTION");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setAlarm(NotificationEntity entity) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("id", entity.getId());
        intent.putExtra("title", entity.getTitle());
        intent.putExtra("content", entity.getDescription());
        int ALARM1_ID = 10000;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, ALARM1_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                entity.getDate(),
                pendingIntent
        );
    }
}