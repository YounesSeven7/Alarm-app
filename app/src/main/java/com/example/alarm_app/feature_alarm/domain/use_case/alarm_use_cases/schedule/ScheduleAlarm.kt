package com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.schedule

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.domain.repository.AlarmScheduling
import com.example.alarm_app.feature_alarm.presentation.receivers.AlarmReceiver
import com.example.alarm_app.feature_alarm.presentation.services.AlarmService
import com.example.alarm_app.feature_alarm.presentation.util.Constants
import com.example.alarm_app.feature_alarm.presentation.util.getRemainingTimeInMinutesToAlarm
import com.example.alarm_app.feature_alarm.presentation.util.getTriggerTime
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class ScheduleAlarm @Inject constructor(
    private val alarmScheduling: AlarmScheduling
) {
    @SuppressLint("ScheduleExactAlarm")
    operator fun invoke(context: Context, alarm: Alarm) {
        alarmScheduling.scheduleAlarm(context, alarm)
    }




}