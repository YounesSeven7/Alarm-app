package com.example.alarm_app.feature_alarm.presentation


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.alarm_app.feature_alarm.presentation.receivers.BootCompletedReceiver
import com.example.alarm_app.feature_alarm.presentation.screens.BottomBarDestination
import com.example.alarm_app.feature_alarm.presentation.screens.ReminderScreen
import com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.add_edit_alarm_screen.AddEditAlarmScreen
import com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.alarm_screen.AlarmScreen
import com.example.alarm_app.feature_alarm.presentation.screens.stopwatch_section.StopwatchScreen
import com.example.alarm_app.feature_alarm.presentation.services.AlarmService
import com.example.alarm_app.feature_alarm.presentation.services.StopwatchService
import com.example.alarm_app.feature_alarm.util.Constants
import com.example.alarm_app.feature_alarm.util.Screen
import com.example.alarm_app.ui.theme.AlarmAppTheme
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)
    private lateinit var stopwatchService: StopwatchService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as StopwatchService.StopwatchBinder
            stopwatchService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StopwatchService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val action = intent.action
        registerBootCompletedReceiver()
        Log.d("younes", "younes")
        setContent {
            AlarmAppTheme {
                when (action) {
                    Constants.ACTION_START_REMINDER_SCREEN -> {
                        showOverLockscreen()
                        ReminderScreen()
                    }
                    Constants.ACTION_STOP_ALARM -> {
                        finish()
                    }
                    else -> {
                        GlobalNavHostSetup()
                    }
                }
            }
        }
    }

    private fun registerBootCompletedReceiver() {
        val receiver = ComponentName(baseContext, BootCompletedReceiver::class.java)

        packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun showOverLockscreen() {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )


        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
    }

    override fun finish() {
        super.finishAndRemoveTask()
    }

    @Composable
    fun ReminderScreen() {
        ReminderScreen(
            onClick = {
                stopService(Intent(baseContext, AlarmService::class.java))
                finish()
            }
        )
    }


    @Composable
    fun GlobalNavHostSetup() {
        val globalNavController = rememberNavController()
        NavHost(navController = globalNavController, startDestination = "start") {
            composable(route = "start") {
                BottomNavSetup(globalNavController = globalNavController)
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
                AddEditAlarmScreen(globalNavController = globalNavController)
            }
        }

    }



    @Composable
    fun BottomNavSetup(globalNavController: NavHostController) {
        val bottomNavController = rememberNavController()
        Scaffold(
            bottomBar = { BottomBar(bottomNavController) }
        ) {
            BottomNavHostSetup(
                bottomNavController = bottomNavController,
                globalNavController = globalNavController,
                modifier = Modifier.padding(it)
            )
        }
    }



    @Composable
    fun BottomNavHostSetup(
        bottomNavController: NavHostController,
        globalNavController: NavHostController,
        modifier: Modifier
    ) {
        val startDestination = getBottomNavStartDestination()
        NavHost(navController = bottomNavController, startDestination = startDestination) {
            composable(route = Screen.AlarmScreen.route) {
                AlarmScreen(
                    globalNavController = globalNavController,
                    modifier = modifier
                )
            }
            composable(route = Screen.Stopwatch.route) {
                if (isBound) StopwatchScreen(
                    stopwatchService = stopwatchService,
                    modifier = modifier
                )
            }
        }
    }

    fun getBottomNavStartDestination(): String {
        val action = intent.action
        return if (action != null && action == Constants.ACTION_START_STOPWATCH_SCREEN)
            Screen.Stopwatch.route
        else
            Screen.AlarmScreen.route
    }


    @Preview
    @Composable
    fun GreetingPreview() {
        //AddEditAlarmScreen(baseContext)
    }

    @Composable
    fun BottomBar(
        navController: NavController
    ) {
        val currentDestination by navController.currentBackStackEntryAsState()
        val destinationRout = currentDestination?.destination?.route ?: ""


        NavigationBar {
            BottomBarDestination.entries.forEach { destination ->
                val label = resources.getString(destination.label)
                NavigationBarItem(
                    selected = destination.rout == destinationRout,
                    onClick = {
                        navController.navigate(destination.rout) {
                            popUpTo(Screen.AlarmScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = resources.getString(destination.label)
                        )
                    },
                    label = {
                        Text(text = label)
                    }

                )
            }
        }
    }

}









