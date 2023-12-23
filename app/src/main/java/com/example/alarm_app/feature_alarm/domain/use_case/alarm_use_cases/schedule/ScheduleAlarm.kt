package com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.schedule
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.domain.repository.AlarmScheduling
import javax.inject.Inject

class ScheduleAlarm @Inject constructor(
    private val alarmScheduling: AlarmScheduling
) {
    @SuppressLint("ScheduleExactAlarm")
    operator fun invoke(alarm: Alarm) {
        alarmScheduling.scheduleAlarm(alarm)
    }

}