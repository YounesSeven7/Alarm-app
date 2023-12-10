package com.example.alarm_app.feature_alarm.presentation.notification.notification_channel

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.presentation.util.Constants

@RequiresApi(Build.VERSION_CODES.O)
fun createAlarmNotificationChannel(notificationManager: NotificationManager) {
    val channelId = Constants.ALARM_NOTIFICATION_CHANNEL_ID
    val channelName = Constants.ALARM_NOTIFICATION_CHANNEL_NAME
    val importance = NotificationManager.IMPORTANCE_HIGH

    val channel = NotificationChannel(channelId, channelName, importance)
    notificationManager.createNotificationChannel(channel)
}

