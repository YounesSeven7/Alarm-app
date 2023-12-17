@file:Suppress("DEPRECATION")

package com.example.alarm_app.feature_alarm.presentation.util

import android.app.AlarmManager
import android.content.Intent
import android.os.Build
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






