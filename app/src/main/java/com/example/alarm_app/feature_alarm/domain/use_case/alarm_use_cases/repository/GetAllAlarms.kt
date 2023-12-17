package com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.repository

import com.example.alarm_app.feature_alarm.data.repository.room.AlarmRepository
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAlarms @Inject constructor (
    private val repository: AlarmRepository
) {
    operator fun invoke(): Flow<List<Alarm>>  {
        return repository.getAllAlarm()
    }
}