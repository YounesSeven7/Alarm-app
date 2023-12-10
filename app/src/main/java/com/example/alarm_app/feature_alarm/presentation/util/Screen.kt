package com.example.alarm_app.feature_alarm.presentation.util

sealed class Screen(val route: String, val idKey: String?) {
    data object AlarmScreen: Screen(route = "alarm_screen", idKey = null)
    data object AddEditAlarmScreen: Screen(route = "add_edit_alarm_screen", idKey = "ALARM_ID")
}