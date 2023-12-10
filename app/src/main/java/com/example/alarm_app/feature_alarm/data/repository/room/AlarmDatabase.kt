package com.example.alarm_app.feature_alarm.data.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.alarm_app.feature_alarm.domain.model.Alarm

@Database(entities = [Alarm::class], version = 1)
abstract class AlarmDatabase: RoomDatabase() {
    abstract val alarmDao: AlarmDao

    companion object {
        const val DATABASE_NAME = "ALARM_DB"
    }
}