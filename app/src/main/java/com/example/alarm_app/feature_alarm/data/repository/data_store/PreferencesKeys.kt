package com.example.alarm_app.feature_alarm.data.repository.data_store

import androidx.datastore.preferences.core.intPreferencesKey
import com.example.alarm_app.feature_alarm.presentation.util.Constants

object PreferencesKeys {
    val scheduledAlarmId = intPreferencesKey(Constants.SCHEDULED_ALARM_ID)
}