package com.example.lab6;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    TextView title, description, time;
    Context context;

    public NotificationViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        this.context = context;

        title = itemView.findViewById(R.id.title_notification_view);
        description = itemView.findViewById(R.id.description_notification);
        time = itemView.findViewById(R.id.time_notification);
    }

    public void onBind(NotificationEntity notification) {
        title.setText(notification.getTitle());
        description.setText(notification.getDescription());
        time.setText(DateUtils.formatDateTime(context,
                notification.getDate(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }
}