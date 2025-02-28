package com.example.frisenleisisensmartcompanion.ui.theme.component

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.frisenleisisensmartcompanion.R

object NotificationHelper {
    private const val CHANNEL_ID = "event_reminder_channel"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "Event Reminders", importance).apply {
                description = "Channel for event reminders"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, title: String, content: String, notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ensure you have an icon here
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(notificationId, notification) // Show notification with specific ID
    }
}

object PreferencesHelper {

    private const val PREF_NAME = "user_preferences"
    private const val KEY_NOTIFY = "key_notify_event"

    // Function to get SharedPreferences
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Function to save notification preference
    fun saveNotificationPreference(context: Context, shouldNotify: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_NOTIFY, shouldNotify)
        editor.apply()  // Apply changes asynchronously
    }

    // Function to get notification preference
    fun getNotificationPreference(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_NOTIFY, false)  // Default is false
    }
}