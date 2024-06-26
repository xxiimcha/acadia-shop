package com.example.acadiashop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(private val notificationList: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivNotificationIcon: ImageView = itemView.findViewById(R.id.ivNotificationIcon)
        val tvNotificationMessage: TextView = itemView.findViewById(R.id.tvNotificationMessage)
        val tvNotificationTime: TextView = itemView.findViewById(R.id.tvNotificationTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.tvNotificationMessage.text = notification.text
        holder.tvNotificationTime.text = notification.time
        // Set the icon for notification, if needed
        holder.ivNotificationIcon.setImageResource(R.drawable.ic_notification)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
}
