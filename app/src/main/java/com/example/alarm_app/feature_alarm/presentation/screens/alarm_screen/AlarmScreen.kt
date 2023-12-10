package com.example.alarm_app.feature_alarm.presentation.screens.alarm_screen

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.schedule.ScheduleAlarm
import com.example.alarm_app.feature_alarm.presentation.notification.notification_channel.createAlarmNotificationChannel
import com.example.alarm_app.feature_alarm.presentation.util.Screen

import java.time.LocalDateTime

@Composable
fun AlarmScreen(
    navController: NavController,
    notificationManager: NotificationManager,
    context: Context,
    scheduleAlarm: ScheduleAlarm
) {

    val hours = 5 + 12
    val minutes = 51

    val alarm = Alarm(
        id = 1,
        timeInMinutes = (hours * 60) + minutes,
        days = 7654321,
        label = "younes",
        soundUri = null,
        isVibrate = true,
        isEnabled = true
    )

    val activity = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createAlarmNotificationChannel(notificationManager)

        scheduleAlarm(context, alarm)
    }



    Scaffold {
        Column(
            modifier = Modifier.padding(top = it.calculateTopPadding())
        ) {
            Button(
                onClick = {
                    val code =Screen.AddEditAlarmScreen.route +
                            "?${Screen.AddEditAlarmScreen.idKey} ={12}"
                    navController.navigate(code)
                }
            ) {
                val time = LocalDateTime.now().plusHours(6).hour
                Text(text = "$time")
            }

            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        activity.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            ) {
                Text(text = "post animation")
            }
        }
    }
}