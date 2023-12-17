package com.example.alarm_app.feature_alarm.data.repository.room

import com.example.alarm_app.feature_alarm.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    fun getAllAlarm(): Flow<List<Alarm>>

    suspend fun getAlarm(id: Int): Alarm?

    suspend fun getAlarmByPickedTime(alarm: Alarm): Alarm?

    fun getEnabledAlarms(): Flow<List<Alarm>>

    suspend fun insertAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)

    suspend fun deleteAlarms(alarmList: List<Alarm>)

}
