package com.example.alarm_app.feature_alarm.presentation.util

import android.util.Log
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import kotlin.math.pow

infix fun Int.pow(other: Int): Int = this.toDouble().pow(other.toDouble()).toInt()

fun isDaySelected(days: Int, dayIndex: Int): Boolean {
    val currentDay = (days / (10 pow dayIndex)) % 10
    return currentDay == (dayIndex + 1)
}

fun getRemainingTimeInMinutesToAlarm(alarm: Alarm): Int {
    val time = LocalDateTime.now()

    val currentDayIndex = time.dayOfWeek.value % 7
    val currentDayNumber = currentDayIndex + 1
    val currentTimeInMinutes = time.hour * 60 + time.minute

    var remainingTimeInMinutes = 0

    if (isDaySelected(alarm.days, currentDayIndex) && alarm.timeInMinutes > currentTimeInMinutes) {
        remainingTimeInMinutes = alarm.timeInMinutes - currentTimeInMinutes
    }
    else {
        var i = currentDayIndex + 1
        remainingTimeInMinutes += (24 * 60)
        while (!isDaySelected(alarm.days, i)) {
            i = (i + 1) % 7
            remainingTimeInMinutes += (24 * 60)
        }
        remainingTimeInMinutes += alarm.timeInMinutes - currentTimeInMinutes
    }

    return remainingTimeInMinutes
}

fun getTriggerTime(remainingTimeInMinutesToAlarm: Int): Long {
    val currentTime = LocalDateTime.now()

    val triggerTime = currentTime
        .plusMinutes(remainingTimeInMinutesToAlarm.toLong())
        .minusSeconds(currentTime.second.toLong())

    Log.d("younes", "$triggerTime")

    return triggerTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
}


