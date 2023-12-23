package com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.schedule

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.domain.repository.AlarmScheduling
import javax.inject.Inject

class CancelScheduledAlarm @Inject constructor(
    private val alarmScheduling: AlarmScheduling
) {
    @SuppressLint("ScheduleExactAlarm")
    operator fun invoke(alarm: Alarm) {
        alarmScheduling.cancelScheduledAlarm(alarm)
    }


}