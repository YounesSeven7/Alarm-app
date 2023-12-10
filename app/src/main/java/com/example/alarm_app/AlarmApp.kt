package com.example.alarm_app

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.preferences.preferencesDataStore
import com.example.alarm_app.feature_alarm.presentation.notification.notification_channel.createAlarmNotificationChannel
import com.example.alarm_app.feature_alarm.presentation.util.Constants
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = Constants.ALARM_PREFERENCES_FILE)

@HiltAndroidApp
class AlarmApp: Application() {



    override fun onCreate() {
        super.onCreate()

    }

}

