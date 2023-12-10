@file:Suppress("UNREACHABLE_CODE")

package com.example.alarm_app.feature_alarm.presentation.notification


import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat




fun checkNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) true
    else ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
}


fun NotificationManagerCompat.showNotification(id: Int, notification: Notification) {


    this.notify(id, notification)
}

fun NotificationManagerCompat.hideNotification(id: Int) {
    this.cancel(id)
}