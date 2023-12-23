package com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.alarm_screen.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.util.Constants
import com.example.alarm_app.feature_alarm.util.fromMinutesToHour
import com.example.alarm_app.feature_alarm.util.isDaySelected
import com.example.alarm_app.feature_alarm.util.isMorning




import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlarmItem(
    alarm: Alarm,
    onChangeEnabledState: (Boolean) -> Unit,
    onClick: () -> Unit,
    deletingMode: Boolean = false
) {


    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 25.dp, horizontal = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                DeletionRadiaButton(
                    deletingMode = deletingMode,
                    isSelected = alarm.isSelectedToDelete
                )

                TimeText(
                    timeInMinutes = alarm.timeInMinutes,
                    isAlarmActive = alarm.isEnabled
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                DaysText(
                    days = alarm.days,
                    isActive = alarm.isEnabled
                )

                Spacer(modifier = Modifier.padding(end = 8.dp))

                Switch(
                    checked = alarm.isEnabled,
                    onCheckedChange = { onChangeEnabledState(it) }
                )
            }
        }
    }
}

@Composable
fun TimeText(
    timeInMinutes: Int,
    isAlarmActive: Boolean
) {
    val hour = fromMinutesToHour(timeInMinutes)
    val minutes = timeInMinutes % 60
    val minutesInFormat = String.format(Locale.US,"%02d", minutes)
    val isMorning = isMorning(timeInMinutes)
    val color: Color = if (isAlarmActive) MaterialTheme.colorScheme.onSurface else Color.Gray

    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 32.sp)) {
                append("$hour:$minutesInFormat")
            }
            if (isMorning) append("AM") else append("PM")
        },
        color = color
    )
}


@Composable
fun DeletionRadiaButton(
    deletingMode: Boolean,
    isSelected: Boolean
) {

    val sizeAnimation by animateDpAsState(
        targetValue = if (deletingMode) 24.dp else 0.dp,
        label = "",
        animationSpec = tween(durationMillis = 500)
    )

    RoundedCheckBox(
        modifier = Modifier.size(sizeAnimation),
        isSelected = isSelected
    )
}


@Composable
fun DaysText(
    days: Int,
    isActive: Boolean
) {
    val color = if (isActive) MaterialTheme.colorScheme.primary else Color.Gray
    if (days == Constants.allDaysSelected) {
        Text(
            text = "Every day",
            color = color
        )
    } else {
        Row {
            for (dayIndex in 0..6) {
                DayItem(
                    dayIndex = dayIndex,
                    isDaySelected = isDaySelected(days, dayIndex),
                    color = color
                )
            }
        }
    }
}

@Composable
fun DayItem(
    dayIndex: Int,
    isDaySelected: Boolean,
    color: Color
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier.width(3.dp)
        ) {
            drawCircle(
                color = if (isDaySelected) color else Color.Transparent,
                radius = (1.5).dp.toPx()
            )
        }
        Text(
            text = "${Constants.daysFirstLetterList[dayIndex]}",
            color = if (isDaySelected) color else Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(
                horizontal = 1.dp,
                vertical = 1.dp
            )
        )
    }
}


