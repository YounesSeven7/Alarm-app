package com.example.alarm_app.feature_alarm.presentation.services

import android.app.KeyguardManager
import android.app.NotificationManager
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.schedule.ScheduleAlarm
import com.example.alarm_app.feature_alarm.presentation.helper.createAlarmNotificationChannel
import com.example.alarm_app.feature_alarm.presentation.helper.getAlarmNotification
import com.example.alarm_app.feature_alarm.util.Constants
import com.example.alarm_app.feature_alarm.util.getParcelableExtraCompat
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

@Suppress("DEPRECATION")
@AndroidEntryPoint
class AlarmService: Service() {

    //@Inject lateinit var powerManager: PowerManager
    @Inject lateinit var notificationManager: NotificationManager
    @Inject lateinit var keyguardManager: KeyguardManager
    @Inject lateinit var scheduleAlarm: ScheduleAlarm
    @Inject lateinit var vibrator: Vibrator
    @Inject lateinit var player: ExoPlayer
    @Inject lateinit var audioManager: AudioManager

    private var vibration: Timer? = null


    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createAlarmNotificationChannel(notificationManager)
        when (intent.action) {
            Constants.ACTION_START_ALARM -> start(intent)
            Constants.ACTION_STOP_ALARM -> stop()
        }
        return START_STICKY
    }

    override fun startForegroundService(service: Intent?): ComponentName? {
        return super.startForegroundService(service)
    }

    private fun start(intent: Intent) {
        val alarm = intent.getParcelableExtraCompat(Constants.EXTRA_ALARM, Alarm::class.java)
        alarm?.let {
            scheduleNextAlarm(alarm)

            val notification = getAlarmNotification(alarm, keyguardManager.isDeviceLocked)
            startForeground(Constants.ALARM_NOTIFICATION_ID, notification)

            if (alarm.isVibrate) startVibration()
            alarm.soundUri?.also {uri -> startRingtone(uri) }
        }
    }

    private fun scheduleNextAlarm(alarm: Alarm) {
        scheduleAlarm(alarm)
    }

    private fun startVibration() {
        vibration = fixedRateTimer(initialDelay = 0L, period = 3000L) {
            vibrationEffect()
        }
    }

    private fun vibrationEffect() {
        vibrator.also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //Todo
                it.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                it.vibrate(2000)
            }
        }
    }

    private fun cancelVibration() {
        vibration?.cancel()
        vibrator.cancel()
    }

    private fun startRingtone(uri: String) {
        audioManager.setStreamVolume(
            AudioManager.STREAM_RING,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),
            0
        )
        audioManager.mode = AudioManager.MODE_RINGTONE
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.repeatMode = ExoPlayer.REPEAT_MODE_ONE
        player.prepare()
        player.play()

    }


    override fun onDestroy() {
        player.stop()
        cancelVibration()
        super.onDestroy()
    }


    private fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
}