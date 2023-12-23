package com.example.alarm_app.feature_alarm.domain.model

import android.os.Parcelable
import android.provider.Settings
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import javax.annotation.concurrent.Immutable


@Parcelize
@Immutable
@Entity
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val timeInMinutes: Int,
    val days: Int,
    val label: String,
    val soundUri: String? = Settings.System.DEFAULT_ALARM_ALERT_URI.toString(),
    val isVibrate: Boolean,
    val isEnabled: Boolean = true,
    @Ignore val isSelectedToDelete: Boolean = false
):  Parcelable {

    constructor(
        id:Int,
        timeInMinutes: Int,
        days: Int,
        label: String,
        soundUri: String?,
        isVibrate: Boolean,
        isEnabled: Boolean,
    ): this(id, timeInMinutes, days, label, soundUri, isVibrate, isEnabled, false)
}
