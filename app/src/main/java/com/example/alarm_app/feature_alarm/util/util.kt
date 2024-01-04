@file:Suppress("DEPRECATION")

package com.example.alarm_app.feature_alarm.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import java.io.Serializable


fun getAlarmDaysText(days: Int): String {
    var alarmDaysText = ""
    when (days) {
        0 -> alarmDaysText = "No day Selected"
        7654321 -> alarmDaysText = "Every day"
        else -> {
            alarmDaysText += "Every "
            for (dayIndex in 0..6) {
                val isSelected = isDaySelected(days, dayIndex)
                if (isSelected) alarmDaysText += Constants.daysNamesList[dayIndex] + ", "
            }
            alarmDaysText = alarmDaysText.substring(0, alarmDaysText.length - 2)
        }
    }
    return alarmDaysText
}






inline fun <reified T : Serializable> Intent.getSerializableExtraCompat(name: String, clazz: Class<T>): T {
    val serializable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getSerializableExtra(Constants.EXTRA_ALARM, T::class.java)
    } else {
        this.getSerializableExtra(Constants.EXTRA_ALARM)
    }
    return serializable as T
}


inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelableExtra(Constants.EXTRA_ALARM, clazz)!!
    } else {
        this.getParcelableExtra(Constants.EXTRA_ALARM)
    }
}

fun getPendingIntentFlag() = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE


@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }






