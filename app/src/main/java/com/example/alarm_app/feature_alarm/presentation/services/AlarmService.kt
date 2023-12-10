package com.example.alarm_app.feature_alarm.presentation.services

import android.app.KeyguardManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.alarm_app.R
import com.example.alarm_app.feature_alarm.MainActivity
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.presentation.util.Constants
import com.example.alarm_app.feature_alarm.presentation.util.getSerializableExtraCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService: Service() {

    @Inject lateinit var powerManager: PowerManager
    @Inject lateinit var keyguardManager: KeyguardManager
    @Inject lateinit var notificationManager: NotificationManager


    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            Constants.ACTION_START_ALARM -> start(intent)
            Constants.ACTION_STOP_ALARM_SERVICE -> stop()
        }
        return START_STICKY
    }

    private fun start(intent: Intent) {
        val alarm = intent.getSerializableExtraCompat(Constants.EXTRA_ALARM, Alarm::class.java)
        val notification = getAlarmNotification(alarm)
        startForeground(Constants.ALARM_NOTIFICATION_ID, notification)
        // Todo grantReadUriPermission(soundUri)

    }

    private fun getAlarmNotification(alarm: Alarm): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            action = Constants.ACTION_START_REMINDER_SCREEN
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Constants.EXTRA_ALARM, alarm)
        }

        val pendingIntent = PendingIntent.getActivity(
            baseContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val hours = alarm.timeInMinutes / 60
        val minutes = alarm.timeInMinutes % 60

        val notification = NotificationCompat.Builder(this, Constants.ALARM_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Alarm")
            .setContentText("$hours:$minutes")
            .setSmallIcon(R.drawable.ic_alarm_vector)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .apply {
                if (keyguardManager.isDeviceLocked)
                    this.setFullScreenIntent(pendingIntent, true)
                else
                    this.setContentIntent(pendingIntent)
            }
            .build()

        return notification
    }


    private fun stop() {

    }


}