package com.example.alarm_app.feature_alarm.presentation.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.alarm_app.R
import com.example.alarm_app.feature_alarm.presentation.MainActivity
import com.example.alarm_app.feature_alarm.presentation.services.StopwatchService
import com.example.alarm_app.feature_alarm.util.Constants
import com.example.alarm_app.feature_alarm.util.fromSecondToTimeFormat
import com.example.alarm_app.feature_alarm.util.getPendingIntentFlag

@RequiresApi(Build.VERSION_CODES.O)
fun getStopwatchNotificationChannel(): NotificationChannel {
    val channelId = Constants.STOPWATCH_NOTIFICATION_CHANNEL_ID
    val channelName = Constants.STOPWATCH_NOTIFICATION_CHANNEL_NAME
    val importance = NotificationManager.IMPORTANCE_LOW
    return NotificationChannel(channelId, channelName, importance)
}

fun Context.getStopwatchNotificationBuilder(timeInSecond: Int): NotificationCompat.Builder {
    return NotificationCompat.Builder(this, Constants.STOPWATCH_NOTIFICATION_CHANNEL_ID)
        .setContentTitle("Stopwatch")
        .setContentText(fromSecondToTimeFormat(timeInSecond))
        .setSmallIcon(R.drawable.ic_timer)
        .setOngoing(true)
        .setContentIntent(stopwatchActivityPendingIntent())
}

fun Context.getStopwatchWithResumeAction(timeInSecond: Int): Notification {
    return getStopwatchNotificationBuilder(timeInSecond)
        .addAction(0, "Reset", resetPendingIntent())
        .addAction(0, "Resume", resumePendingIntent())
        .build()
}

fun Context.getStopwatchWithStopAction(timeInSecond: Int): Notification {
    return getStopwatchNotificationBuilder(timeInSecond)
        .addAction(0, "Stop", stopPendingIntent())
        .build()
}



private fun Context.resumePendingIntent(): PendingIntent {
    val intent = Intent(this, StopwatchService::class.java).apply {
        action = Constants.ACTION_START_RESUME_STOPWATCH
    }
    return PendingIntent.getService(
        this,
        0,
        intent,
        getPendingIntentFlag()
    )
}

private fun Context.stopPendingIntent(): PendingIntent {
    val intent = Intent(this, StopwatchService::class.java).apply {
        action = Constants.ACTION_STOP_STOPWATCH
    }
    return PendingIntent.getService(
        this,
        0,
        intent,
        getPendingIntentFlag()
    )
}

private fun Context.resetPendingIntent(): PendingIntent {
    val intent = Intent(this, StopwatchService::class.java).apply {
        action = Constants.ACTION_RESET_STOPWATCH
    }
    return PendingIntent.getService(
        this,
        0,
        intent,
        getPendingIntentFlag()
    )

}

private fun Context.stopwatchActivityPendingIntent(): PendingIntent {
    val intent = Intent(this, MainActivity::class.java).apply {
        action = Constants.ACTION_START_STOPWATCH_SCREEN
    }
    return PendingIntent.getActivity(
        this,
        0,
        intent,
        getPendingIntentFlag()
    )
}




