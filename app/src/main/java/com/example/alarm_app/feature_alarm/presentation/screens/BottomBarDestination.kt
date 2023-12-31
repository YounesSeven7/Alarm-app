package com.example.alarm_app.feature_alarm.presentation.screens

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.alarm_app.R
import com.example.alarm_app.feature_alarm.util.Screen


enum class BottomBarDestination(
    val rout: String,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Alarm(Screen.AlarmScreen.route, Icons.Default.Alarm, R.string.alarm),
    Stopwatch(Screen.Stopwatch.route, Icons.Default.Timer, R.string.stopwatch),
    //Timer(Screen.Timer.rout, Icons.Default.HourglassBottom, R.string.timer)
}