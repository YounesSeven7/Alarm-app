package com.example.alarm_app.feature_alarm.presentation.screens.add_edi_allarm_screen

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.repository.AddAlarm
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.repository.GetAlarmByPickedTime
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.schedule.ScheduleAlarm
import com.example.alarm_app.feature_alarm.presentation.util.isDaySelected
import com.example.alarm_app.feature_alarm.presentation.util.pow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditAlarmViewModel @Inject constructor(
    val context: Application,
    val addAlarm: AddAlarm,
    val getAlarmByPickedTime: GetAlarmByPickedTime,
    val scheduleAlarm: ScheduleAlarm
): ViewModel() {

    var timeInMinutes = mutableIntStateOf(60)

    var isMorning = mutableStateOf(true)

    var selectedDats = mutableIntStateOf(0)

    var labelValue =  mutableStateOf("")

    var isAlarmSoundEnabled =  mutableStateOf(true)
    var alarmSoundName =  mutableStateOf(getDefaultRingtoneName(context))

    var alarmSoundUri: String? = null

    var isVibrationEnabled = mutableStateOf(true)



    fun addUpdateAlarm(context: Context, onComplete: () -> Unit) {
        var newAlarm = getAlarmData()
        viewModelScope.launch {
            val alarmInSameTime = getAlarmByPickedTime(newAlarm.timeInMinutes)

            if (alarmInSameTime != null) {
                val newSelectedDays = getNewSelectedDates(alarmInSameTime.days, newAlarm.days)
                newAlarm = alarmInSameTime.copy(days = newSelectedDays)
            } else {
                scheduleAlarm(context, newAlarm)
            }
            addAlarm(newAlarm)
            onComplete()
        }
    }

    private fun getNewSelectedDates(
        alarmInSameTimeDays: Int,
        newAlarmDays: Int
    ): Int {
        var commonDays = 0
        for (dayIndex in 0..6) {
            if (isDaySelected(alarmInSameTimeDays, dayIndex) || isDaySelected(newAlarmDays, dayIndex)) {
                commonDays += (10 pow dayIndex) * (dayIndex + 1)
            }
        }
        return commonDays
    }

    private fun getAlarmData() = Alarm(
        timeInMinutes = timeInMinutes.intValue + if (isMorning.value) 0 else 12 * 60,
        days = selectedDats.intValue,
        label = labelValue.value,
        soundUri = alarmSoundUri,
        isVibrate = isVibrationEnabled.value
    )

}
