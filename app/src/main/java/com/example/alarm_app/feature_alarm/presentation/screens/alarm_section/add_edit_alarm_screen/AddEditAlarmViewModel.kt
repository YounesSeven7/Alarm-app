package com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.add_edit_alarm_screen

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.repository.AddAlarm
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.repository.DeleteAlarm
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.repository.GetAlarm
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.repository.GetAlarmByPickedTime
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.schedule.ScheduleAlarm
import com.example.alarm_app.feature_alarm.presentation.util.Screen
import com.example.alarm_app.feature_alarm.presentation.util.isDaySelected
import com.example.alarm_app.feature_alarm.presentation.util.isMorning
import com.example.alarm_app.feature_alarm.presentation.util.pow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditAlarmViewModel @Inject constructor(
    val context: Application,
    val addAlarm: AddAlarm,
    val getAlarmByPickedTime: GetAlarmByPickedTime,
    val deleteAlarm: DeleteAlarm,
    val getAlarm: GetAlarm,
    val scheduleAlarm: ScheduleAlarm,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private var id:Int? = null

    var timeInMinutes = mutableIntStateOf(60)

    var isMorning = mutableStateOf(true)

    var selectedDats = mutableIntStateOf(0)

    var labelValue =  mutableStateOf("")

    var isAlarmSoundEnabled =  mutableStateOf(true)
    var alarmSoundName =  mutableStateOf(getDefaultRingtoneName(context))

    var alarmSoundUri: String? = null

    var isVibrationEnabled = mutableStateOf(true)

    init {
        id = savedStateHandle.get<Int>(Screen.AddEditAlarmScreen.Key!!)?.also { id ->
            viewModelScope.launch {
                val alarm = getAlarm(id)
                alarm?.let {
                    timeInMinutes.intValue = if (alarm.timeInMinutes < 60) alarm.timeInMinutes
                    else if ((alarm.timeInMinutes / 60) in 1..12) alarm.timeInMinutes
                    else ((alarm.timeInMinutes / 60) % 12) * 60 + alarm.timeInMinutes % 60

                    isMorning.value = isMorning(alarm.timeInMinutes)

                    selectedDats.intValue= alarm.days

                    labelValue.value = alarm.label

                    isAlarmSoundEnabled.value = alarm.soundUri != null
                    alarmSoundUri = alarm.soundUri
                    alarmSoundName.value =
                        if (alarmSoundUri != null) getRingtoneName(context, Uri.parse(alarmSoundUri))
                        else "Silent"

                    isVibrationEnabled.value = alarm.isVibrate
                }
            }
        }
    }

    fun addUpdateAlarm(context: Context, navController: NavController) {
        var newAlarm = getAlarmData()
        viewModelScope.launch {
            val alarmInSameTime = getAlarmByPickedTime(newAlarm)

            if (alarmInSameTime != null) {
                newAlarm.id?.also {
                    deleteAlarm(newAlarm)
                    id = alarmInSameTime.id
                }

                val newSelectedDays = getNewSelectedDates(alarmInSameTime.days, newAlarm.days)
                alarmInSameTime.copy(days = newSelectedDays).also { newAlarm = it }
            }
            addAlarm(newAlarm)
            scheduleAlarm(context, newAlarm)
            navController.popBackStack()
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
        id = if (id == -1) null else id,
        timeInMinutes =
            if (isMorning.value && timeInMinutes.intValue / 60 == 12) timeInMinutes.intValue % 60
            else if (isMorning.value || timeInMinutes.intValue / 60 == 12) timeInMinutes.intValue
            else timeInMinutes.intValue +  12 * 60,
        days = selectedDats.intValue,
        label = labelValue.value,
        soundUri = alarmSoundUri,
        isVibrate = isVibrationEnabled.value,
        isEnabled = true
    )

}
