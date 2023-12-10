@file:OptIn(ExperimentalAnimationApi::class)

package com.example.alarm_app.feature_alarm


import android.app.NotificationManager

import android.os.Build
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.animation.ExperimentalAnimationApi

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalDensity


import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.schedule.ScheduleAlarm
import com.example.alarm_app.feature_alarm.presentation.notification.notification_channel.createAlarmNotificationChannel
import com.example.alarm_app.feature_alarm.presentation.screens.add_edi_allarm_screen.AddEditAlarmScreen
import com.example.alarm_app.feature_alarm.presentation.screens.add_edi_allarm_screen.TimePicker
import com.example.alarm_app.feature_alarm.presentation.screens.alarm_screen.AlarmScreen
import com.example.alarm_app.feature_alarm.presentation.util.Constants
import com.example.alarm_app.feature_alarm.presentation.util.Screen
import com.example.alarm_app.feature_alarm.presentation.util.getAlarmDaysText
import com.example.alarm_app.feature_alarm.presentation.util.isDaySelected
import com.example.alarm_app.feature_alarm.presentation.util.pow
import com.example.alarm_app.ui.theme.AlarmAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.prefs.Preferences
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var scheduleAlarm: ScheduleAlarm
    @Inject lateinit var notificationManager: NotificationManager





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlarmAppTheme {


                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.AlarmScreen.route) {
                    composable(route = Screen.AlarmScreen.route) {
                        AlarmScreen(navController = navController, notificationManager, baseContext, scheduleAlarm)
                    }
                    val alarmId = Screen.AddEditAlarmScreen.route
                    composable(
                        route = Screen.AddEditAlarmScreen.route + "?$alarmId={$alarmId}",
                        arguments = listOf(
                            navArgument("${Screen.AddEditAlarmScreen.idKey}") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        AddEditAlarmScreen(context = baseContext, navController = navController)
                    }
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

}


@Preview
@Composable
fun GreetingPreview() {
   //AddEditAlarmScreen(baseContext)
}


@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun TextField() {
    var value by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = { value += it },
        label = { Text("Label") },
        singleLine = true
    )
}



