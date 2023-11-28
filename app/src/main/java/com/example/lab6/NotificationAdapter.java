package com.example.lab6;

import android.app.NotificationManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NotificationEntity> notifications;
    Context context;

    public NotificationAdapter(Context context) {
        this.context = context;
        notifications = new ArrayList<>();
    }

    public void setNotifications(ArrayList<NotificationEntity> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(context).inflate(R.layout.notification_view, parent, false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        NotificationViewHolder holder = (NotificationViewHolder) viewHolder;
        position = holder.getAdapterPosition();

        holder.onBind(notifications.get(position));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void onItemDelete(int adapterPosition) {
        LabApplication app = LabApplication.getInstance(context);
        int id =notifications.get(adapterPosition).getId();
        app.getRepository().delete(id);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}
