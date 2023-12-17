@file:OptIn(ExperimentalAnimationApi::class)
@file:Suppress("DEPRECATION")

package com.example.alarm_app.feature_alarm


import android.annotation.SuppressLint
import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.schedule.ScheduleAlarm
import com.example.alarm_app.feature_alarm.presentation.screens.BottomBarDestination
import com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.add_edit_alarm_screen.AddEditAlarmScreen
import com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.alarm_screen.AlarmScreen
import com.example.alarm_app.feature_alarm.presentation.screens.stopwatch_section.StopwatchScreen
import com.example.alarm_app.feature_alarm.presentation.util.Screen
import com.example.alarm_app.ui.theme.AlarmAppTheme

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var scheduleAlarm: ScheduleAlarm
    @Inject lateinit var notificationManager: NotificationManager



    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlarmAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomBar(navController) }
                ) { _: PaddingValues ->
                    NavHostSetup(navController)
                }
            }
        }
    }
}



@Composable
fun NavHostSetup(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.AlarmScreen.route) {
        composable(route = Screen.AlarmScreen.route) {
            AlarmScreen(navController = navController)
        }
        val alarmIdKey = Screen.AddEditAlarmScreen.Key!!
        composable(
            route = Screen.AddEditAlarmScreen.route + "?$alarmIdKey={$alarmIdKey}",
            arguments = listOf(
                navArgument(name = alarmIdKey) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            AddEditAlarmScreen(
                navController = navController
            )
        }
        composable(route = Screen.Stopwatch.route) {
            StopwatchScreen()
        }
    }
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
fun BottomBar(
    navController: NavController
) {
    val currentDestination by navController.currentBackStackEntryAsState()
    val destinationRout = currentDestination?.destination?.route ?: ""

    NavigationBar {
        BottomBarDestination.entries.forEach { destination ->
            BottomNavigationItem(
                selected =  destinationRout == destination.rout,
                onClick = {
                    navController.navigate(destination.rout)
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.name
                    )
                }
            )
        }
    }


}


