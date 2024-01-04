package com.example.alarm_app.feature_alarm.presentation.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.repository.GetAllAlarms
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.schedule.ScheduleAlarm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject lateinit var getAllAlarms: GetAllAlarms
    @Inject lateinit var ScheduleAlarm: ScheduleAlarm

    @DelicateCoroutinesApi
    override fun onReceive(context: Context, intent: Intent) {
        intent.action
        GlobalScope.launch(Dispatchers.IO) {
            getAllAlarms().collect{ alarmsList ->
                alarmsList.forEach { alarm ->
                    if (alarm.isEnabled)
                    ScheduleAlarm(alarm)
                }
            }
        }

    }
}