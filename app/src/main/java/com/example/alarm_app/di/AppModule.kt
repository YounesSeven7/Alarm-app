package com.example.alarm_app.di

import android.app.AlarmManager
import android.app.Application
import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.PowerManager
import android.os.Vibrator
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.example.alarm_app.feature_alarm.data.repository.room.AlarmDatabase
import com.example.alarm_app.feature_alarm.data.repository.room.AlarmRepository
import com.example.alarm_app.feature_alarm.domain.repository.AlarmRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAlarmDatabase(app: Application): AlarmDatabase {
        return Room.databaseBuilder(
            app,
            AlarmDatabase::class.java,
            AlarmDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAlarmRepository(db: AlarmDatabase): AlarmRepository {
        return AlarmRepositoryImpl(db.alarmDao)
    }

    @Provides
    @Singleton
    fun provideKeyguardManager(context: Application): KeyguardManager =
        context.getSystemService(KeyguardManager::class.java)

    @Provides
    @Singleton
    fun providePowerManager(context: Application): PowerManager =
        context.getSystemService(PowerManager::class.java)

    @Provides
    @Singleton
    fun provideNotificationManagerCompat(context: Application): NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    @Provides
    @Singleton
    fun provideAlarmManager(context: Application): AlarmManager =
        context.getSystemService(AlarmManager::class.java)

    @Provides
    @Singleton
    fun provideVibrator(context: Application): Vibrator =
        context.getSystemService(Vibrator::class.java)

    @Provides
    @Singleton
    fun provideExoPlayer(context: Application): ExoPlayer =
        ExoPlayer.Builder(context).build()

    @Provides
    @Singleton
    fun provideAudioManager(context: Application): AudioManager =
        context.getSystemService(AudioManager::class.java)


}