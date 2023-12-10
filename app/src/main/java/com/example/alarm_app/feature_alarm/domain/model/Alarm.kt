package com.example.alarm_app.feature_alarm.domain.model

import android.provider.Settings
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int = -1,
    val timeInMinutes: Int,
    val days: Int,
    val label: String,
    val soundUri: String? = Settings.System.DEFAULT_ALARM_ALERT_URI.toString(),
    val isVibrate: Boolean,
    val isEnabled: Boolean = true,
) : Serializable
