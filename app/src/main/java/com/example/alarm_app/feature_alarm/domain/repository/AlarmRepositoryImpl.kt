package com.example.alarm_app.feature_alarm.domain.repository

import com.example.alarm_app.feature_alarm.data.repository.room.AlarmDao
import com.example.alarm_app.feature_alarm.data.repository.room.AlarmRepository
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

class AlarmRepositoryImpl(private val dao: AlarmDao): AlarmRepository {

    override fun getAllAlarm(): Flow<List<Alarm>> {
        return dao.getAllAlarms()
    }

    override suspend  fun getAlarm(id: Int): Alarm? {
        return dao.getAlarm(id)
    }

    override suspend fun getAlarmByPickedTime(alarm: Alarm): Alarm? {
        return dao.getAlarmByPickedTime(alarm.id, alarm.timeInMinutes)
    }

    override fun getEnabledAlarms(): Flow<List<Alarm>> {
        return dao.getEnabledAlarms()
    }

    override suspend fun insertAlarm(alarm: Alarm) {
        return dao.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        return dao.deleteAlarm(alarm)
    }

    override suspend fun deleteAlarms(alarmList: List<Alarm>) {
        return dao.deleteAlarms(alarmList)
    }
}