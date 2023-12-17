package com.example.alarm_app.feature_alarm.presentation.util

sealed class Screen(val route: String, val Key: String?) {
    data object AlarmScreen: Screen(route = "alarm_screen", Key = null)
    data object AddEditAlarmScreen: Screen(route = "add_edit_alarm_screen", Key = "ALARM_Id")
    data object Stopwatch: Screen(route = "stopwatch", Key = null)
}