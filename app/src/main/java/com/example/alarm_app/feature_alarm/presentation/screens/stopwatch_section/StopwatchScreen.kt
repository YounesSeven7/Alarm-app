package com.example.alarm_app.feature_alarm.presentation.screens.stopwatch_section


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.alarm_app.feature_alarm.presentation.notification.triggerStopwatchService
import com.example.alarm_app.feature_alarm.presentation.services.StopwatchService
import com.example.alarm_app.feature_alarm.util.Constants
import com.example.alarm_app.feature_alarm.util.fromSecondToTimeFormat
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun StopwatchScreen(
    stopwatchService: StopwatchService
) {

    val timeInSeconds by stopwatchService.timeInSecond
    val timerState by stopwatchService.timerState

    val context = LocalContext.current


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = fromSecondToTimeFormat(timeInSeconds),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge,

            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {
                        when(timerState) {
                            StopwatchState.Stopped ->
                                context.triggerStopwatchService(Constants.ACTION_RESET_STOPWATCH)
                            else -> Unit
                        }
                    },
                    enabled = timerState == StopwatchState.Stopped
                ) {
                    Text("Reset")
                }

                Button(
                    onClick = {
                        when(timerState) {
                            StopwatchState.Started ->
                                context.triggerStopwatchService(Constants.ACTION_STOP_STOPWATCH)
                            else ->
                                context.triggerStopwatchService(Constants.ACTION_START_RESUME_STOPWATCH)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when(timerState) {
                            StopwatchState.Started -> Color(0xFFB3261E)
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                ) {
                    Text(
                        text = when(timerState) {
                            StopwatchState.Idle -> "Start"
                            StopwatchState.Started -> "Stop"
                            StopwatchState.Stopped -> "Resume"
                        }
                    )
                }
            }

        }
    }
}