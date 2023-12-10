package com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.repository

import com.example.alarm_app.feature_alarm.data.repository.room.AlarmRepository
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import javax.inject.Inject

class GetAlarmByPickedTime @Inject constructor (
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(pickedTimeInMinutes: Int): Alarm? {
        return repository.getAlarmByPickedTime(pickedTimeInMinutes)
    }
}