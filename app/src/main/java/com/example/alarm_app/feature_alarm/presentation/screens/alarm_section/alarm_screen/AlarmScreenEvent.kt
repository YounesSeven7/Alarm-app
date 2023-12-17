package com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.alarm_screen

import androidx.navigation.NavController
import com.example.alarm_app.feature_alarm.domain.model.Alarm

sealed class AlarmScreenEvent {
    data class AddAlarm(val navController: NavController): AlarmScreenEvent()
    data class EditAlarm(val navController: NavController, val alarmIndex: Int): AlarmScreenEvent()
    data class OnChangeEnabledState(val alarmIndex: Int,val isEnabled: Boolean): AlarmScreenEvent()
    data object CreateAlarmNotificationChanel: AlarmScreenEvent()
    data class OnChangeDeletionState(val alarmIndex: Int, val isSelectedToDelete: Boolean): AlarmScreenEvent()
    data object DisabledDeletionMode: AlarmScreenEvent()
    data object DeleteAlarms: AlarmScreenEvent()
}