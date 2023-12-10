package com.example.alarm_app.feature_alarm.domain.repository

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.presentation.services.AlarmService
import com.example.alarm_app.feature_alarm.presentation.util.Constants
import com.example.alarm_app.feature_alarm.presentation.util.getRemainingTimeInMinutesToAlarm
import com.example.alarm_app.feature_alarm.presentation.util.getTriggerTime
import javax.inject.Inject

class AlarmScheduling @Inject constructor(
    private val alarmManager: AlarmManager
) {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleAlarm(context: Context, alarm: Alarm) {
        val intent = getIntent(context, alarm)
        val pendingIntent = context.getPendingIntent(intent, alarm)

        val remainingTimeInMinutesToAlarm = getRemainingTimeInMinutesToAlarm(alarm)
        val triggerAt = getTriggerTime(remainingTimeInMinutesToAlarm)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            pendingIntent
        )
    }

    fun cancelScheduledAlarm(context: Context, alarm: Alarm) {
        val intent = getIntent(context, alarm)
        val pendingIntent = context.getPendingIntent(intent, alarm)
        alarmManager.cancel(pendingIntent)
    }

    private fun getIntent(context: Context, alarm: Alarm) =  Intent(context, AlarmService::class.java)
        .apply {
            action = Constants.ACTION_START_ALARM
            putExtra(Constants.EXTRA_ALARM, alarm)
        }

    private fun Context.getPendingIntent(intent: Intent, alarm: Alarm) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                this,
                alarm.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )!!
        } else {
            PendingIntent.getService(
                this,
                alarm.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

}