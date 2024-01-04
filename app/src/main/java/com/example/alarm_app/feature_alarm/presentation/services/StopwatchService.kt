package com.example.alarm_app.feature_alarm.presentation.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.example.alarm_app.feature_alarm.presentation.helper.getStopwatchNotificationChannel
import com.example.alarm_app.feature_alarm.presentation.helper.getStopwatchWithResumeAction
import com.example.alarm_app.feature_alarm.presentation.helper.getStopwatchWithStopAction
import com.example.alarm_app.feature_alarm.presentation.screens.stopwatch_section.StopwatchState
import com.example.alarm_app.feature_alarm.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

@AndroidEntryPoint
class StopwatchService: Service() {

    @Inject lateinit var notificationManager: NotificationManager

    private var timer: Timer? = null

    var timeInSecond = mutableIntStateOf(0)
        private set
    var timerState = mutableStateOf(StopwatchState.Idle)
        private set



    override fun onBind(intent: Intent?): IBinder = StopwatchBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action.also {
            when(it) {
                Constants.ACTION_START_RESUME_STOPWATCH -> {
                    startForegroundService()
                    setStopButton()
                    startStopwatch()
                }
                Constants.ACTION_STOP_STOPWATCH -> {
                    setResumeButton()
                    stopStopwatch()
                }
                Constants.ACTION_RESET_STOPWATCH -> {
                    cancelStopwatch()
                    stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = getStopwatchNotificationChannel()
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startForegroundService() {
        createNotificationChannel()
        val notification = getStopwatchWithStopAction(timeInSecond.intValue)
        startForeground(Constants.STOPWATCH_NOTIFICATION_ID, notification)
    }

    private fun stopForegroundService() {
        notificationManager.cancel(Constants.STOPWATCH_NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun setStopButton() {
        val notification = getStopwatchWithStopAction(timeInSecond.intValue)
        notificationManager.notify(Constants.STOPWATCH_NOTIFICATION_ID, notification)
    }

    private fun setResumeButton() {
        val notification = getStopwatchWithResumeAction(timeInSecond.intValue)
        notificationManager.notify(Constants.STOPWATCH_NOTIFICATION_ID, notification)
    }

    private fun startStopwatch() {
        timerState.value = StopwatchState.Started
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            timeInSecond.intValue += 1
            updateStopwatchNotification()
        }
    }

    private fun stopStopwatch() {
        timer?.cancel()
        timerState.value = StopwatchState.Stopped
    }

    private fun cancelStopwatch() {
        timer = null
        timeInSecond.intValue = 0
        timerState.value = StopwatchState.Idle
    }

    private fun updateStopwatchNotification() {
        val notification = getStopwatchWithStopAction(timeInSecond.intValue)
        notificationManager.notify(Constants.STOPWATCH_NOTIFICATION_ID, notification)
    }

    inner class StopwatchBinder : Binder() {
        fun getService(): StopwatchService = this@StopwatchService
    }
}