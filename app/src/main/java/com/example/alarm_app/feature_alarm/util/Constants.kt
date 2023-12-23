package com.example.alarm_app.feature_alarm.util

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
    const val EARLY_ALARM_NOTIFICATION_ID = "EARLY_ALARM_NOTIFICATION_ID"
    const val EARLY_ALARM_CHANNEL_NAME = "Early alarm dismissal"
    const val EARLY_ALARM__NOTIFICATION = 1

    const val ALARM_NOTIFICATION_CHANNEL_ID = "ALARM_NOTIFICATION_CHANNEL_ID"
    const val ALARM_NOTIFICATION_CHANNEL_NAME = "Alarm"
    const val ALARM_NOTIFICATION_ID = 2

    const val STOPWATCH_NOTIFICATION_CHANNEL_ID = "STOPWATCH_NOTIFICATION_CHANNEL_ID"
    const val STOPWATCH_NOTIFICATION_CHANNEL_NAME = "Stopwatch"
    const val STOPWATCH_NOTIFICATION_ID = 3



    // intent
    const val EXTRA_ALARM = "EXTRA_ALARM"
    const val ACTION_START_ALARM = "ACTION_START_ALARM"
    const val ACTION_START_REMINDER_SCREEN = "ACTION_START_REMINDER_SCREEN"
    const val ACTION_STOP_ALARM = "ACTION_STOP_ALARM"

    const val ACTION_START_RESUME_STOPWATCH = "ACTION_START_RESUME_STOPWATCH"
    const val ACTION_STOP_STOPWATCH = "ACTION_STOP_STOPWATCH"
    const val ACTION_RESET_STOPWATCH = "ACTION_RESET_STOPWATCH"
    const val ACTION_START_STOPWATCH_SCREEN = "ACTION_START_STOPWATCH_SCREEN"





}