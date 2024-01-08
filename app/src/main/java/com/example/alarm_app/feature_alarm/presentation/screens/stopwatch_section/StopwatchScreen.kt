package com.example.alarm_app.feature_alarm.presentation.screens.stopwatch_section


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.alarm_app.feature_alarm.presentation.helper.triggerStopwatchService
import com.example.alarm_app.feature_alarm.presentation.services.StopwatchService
import com.example.alarm_app.feature_alarm.util.Constants
import com.example.alarm_app.feature_alarm.util.fromSecondToTimeFormat

@Composable
fun StopwatchScreen(
    stopwatchService: StopwatchService,
    modifier: Modifier
) {
    val timeInSeconds by stopwatchService.timeInSecond
    val timerState by stopwatchService.timerState

    val context = LocalContext.current

    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(top = 90.dp, bottom = 16.dp)
        ) {

            StopwatchText(time = fromSecondToTimeFormat(timeInSeconds))



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
                    enabled = timerState == StopwatchState.Stopped,
                    modifier = Modifier.scale(1.2f)
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
                    ),
                    modifier = Modifier.scale(1.2f)
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


@Composable
fun StopwatchText(
    time: String
) {
    var oldTime by remember { mutableStateOf(time) }

    SideEffect { oldTime = time }

    Row {
        for (i in oldTime.indices) {
            val oldChar = oldTime.getOrNull(i)
            val newChar = time[i]
            val char = if (newChar == oldChar) oldChar else newChar

            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically { it  }.togetherWith(slideOutVertically { -it })
                }
                , label = ""
            ) {
                Text(
                    text = it.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.displayLarge,
                )
            }

        }
    }
}