package com.example.aesculapius.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.aesculapius.R

class MetricsAlarm : BroadcastReceiver() {
    private var notificationsCount = 0
    override fun onReceive(context: Context, intent: Intent) {
        try {
            showNotification(
                context,
                intent.getStringExtra("title") ?: "Напоминание",
                intent.getStringExtra("message") ?: "Введите метрики с пикфлоуметра",
                notificationsCount
            )
            notificationsCount++
        } catch (ex: Exception) {
            Log.d("Receive Ex", "onReceive: ${ex.printStackTrace()}")
        }
    }
}

private fun showNotification(context: Context, title: String, desc: String, notificationCount: Int) {
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "message_channel"
    val channelName = "message_name"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setContentTitle(title)
        .setContentText(desc)
        .setSmallIcon(R.drawable.edit_icon)

    manager.notify(notificationCount, builder.build())
}