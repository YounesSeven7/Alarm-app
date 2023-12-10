package com.example.alarm_app.feature_alarm.presentation.util

object Constants {

    val daysFirstLetterList = listOf('S', 'M', 'T', 'W', 'T', 'F', 'S')
    val daysNamesList = listOf("Sun" , "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    const val allDaysSelected = 7654321
    const val AM = "AM"
    const val PM = "PM"
    val dayParts = listOf(AM, PM)

    const val ON = "On"
    const val OFF = "Off"

    // data store
    const val ALARM_PREFERENCES_FILE = "ALARM_PREFERENCES_FILE"

    const val SCHEDULED_ALARM_ID = "SCHEDULED_ALARM_ID"

    // notification
    const val ALARM_NOTIFICATION_CHANNEL_ID = "ALARM_NOTIFICATION_CHANNEL_ID"
    const val ALARM_NOTIFICATION_CHANNEL_NAME = "Alarm"
    const val ALARM_NOTIFICATION_ID = 1

    const val EARLY_ALARM_NOTIFICATION_ID = "EARLY_ALARM_NOTIFICATION_ID"
    const val EARLY_ALARM_CHANNEL_NAME = "Early alarm dismissal"
    const val EARLY_ALARM__NOTIFICATION = 2

    const val TIMER_NOTIFICATION_ID = "TIMER_NOTIFICATION_ID"


    // intent
    const val EXTRA_ALARM = "EXTRA_ALARM"
    const val ACTION_START_ALARM = "ACTION_START_ALARM"
    const val ACTION_START_REMINDER_SCREEN = "ACTION_START_REMINDER_SCREEN"
    //const val ACTION_LOCKED_SCREEN_START_ALARM_SERVICE = "ACTION_LOCKED_SCREEN_START_ALARM_SERVICE"
    //const val ACTION_UNLOCKED_SCREEN_START_ALARM_SERVICE = "ACTION_LOCKED_SCREEN_START_ALARM_SERVICE"
    const val ACTION_STOP_ALARM_SERVICE = "ACTION_STOP_ALARM_SERVICE"




}