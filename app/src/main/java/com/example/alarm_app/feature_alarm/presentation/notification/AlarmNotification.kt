package com.example.alarm_app.feature_alarm.presentation.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.alarm_app.R
import com.example.alarm_app.feature_alarm.presentation.MainActivity
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.presentation.services.AlarmService
import com.example.alarm_app.feature_alarm.presentation.services.StopwatchService
import com.example.alarm_app.feature_alarm.util.Constants
import com.example.alarm_app.feature_alarm.util.getPendingIntentFlag

fun createAlarmNotificationChannel(notificationManager: NotificationManager) {
    val channelId = Constants.ALARM_NOTIFICATION_CHANNEL_ID
    val channelName = Constants.ALARM_NOTIFICATION_CHANNEL_NAME
    val importance = NotificationManager.IMPORTANCE_HIGH

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, importance)
        notificationManager.createNotificationChannel(channel)
    }
}


fun Context.getAlarmNotification(alarm: Alarm, isDeviceLocked: Boolean): Notification {
    val reminderActivityPendingIntent = reminderActivityPendingIntent(alarm)
    val stopServicePendingIntent = stopServicePendingIntent()

    val hours = alarm.timeInMinutes / 60
    val minutes = alarm.timeInMinutes % 60

    val notification = NotificationCompat.Builder(this, Constants.ALARM_NOTIFICATION_CHANNEL_ID)
        .setContentTitle("Alarm")
        .setContentText("$hours:$minutes")
        .setSmallIcon(R.drawable.ic_alarm_vector)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .addAction(0, "Stop", stopServicePendingIntent)
        .setStyle(NotificationCompat.BigTextStyle())
        .apply {
            if (isDeviceLocked)
                this.setFullScreenIntent(reminderActivityPendingIntent, true)
        }
        .build()

    return notification
}

private fun Context.reminderActivityPendingIntent(alarm: Alarm): PendingIntent {
    val intent = Intent(this, MainActivity::class.java).apply {
        action = Constants.ACTION_START_REMINDER_SCREEN
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra(Constants.EXTRA_ALARM, alarm)
    }
    return PendingIntent.getActivity(
        this,
        0,
        intent,
        getPendingIntentFlag()
    )
}

private fun Context.stopServicePendingIntent(): PendingIntent {
    val intent = Intent(this, AlarmService::class.java).apply {
        action = Constants.ACTION_STOP_ALARM
    }
    return PendingIntent.getService(
        this,
        0,
        intent,
        getPendingIntentFlag()
    )
}

fun Context.triggerStopwatchService(action: String) {
    Intent(this, StopwatchService::class.java).apply {
        this.action = action
        this@triggerStopwatchService.startService(this)
    }
}

