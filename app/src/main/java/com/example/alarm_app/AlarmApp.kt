package com.example.alarm_app

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.alarm_app.feature_alarm.util.Constants
import dagger.hilt.android.HiltAndroidApp

val Context.dataStore by preferencesDataStore(name = Constants.ALARM_PREFERENCES_FILE)

@HiltAndroidApp
class AlarmApp: Application() {



    override fun onCreate() {
        super.onCreate()

    }

}

