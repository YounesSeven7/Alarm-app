package com.example.alarm_app.feature_alarm.presentation.screens.add_edi_allarm_screen

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.TextButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.alarm_app.feature_alarm.dpToPx
import com.example.alarm_app.feature_alarm.presentation.util.Constants
import com.example.alarm_app.feature_alarm.presentation.util.getAlarmDaysText
import com.example.alarm_app.feature_alarm.presentation.util.isDaySelected
import com.example.alarm_app.feature_alarm.presentation.util.pow

@Composable
fun TimePicker(
    timeInMinutes: Int = 62,
    isMorning: Boolean = true,
    onTimeChange: (timeInMinutes: Int) -> Unit = {},
    dayPartChange: (isMorning: Boolean) -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        NumberPicker(
            modifier = Modifier.weight(1f),
            dividersColor = Color.Transparent,
            textStyle = TextStyle(
                fontSize = 36.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            value = timeInMinutes / 60,
            onValueChange = { onTimeChange((it * 60) + (timeInMinutes % 60)) },
            range = 1..12
        )
        Text(
            text = ":",
            fontSize = 36.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        NumberPicker(
            modifier = Modifier
                .weight(1f)
                ,
            dividersColor = Color.Transparent,
            textStyle = TextStyle(
                fontSize = 36.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            range = 0..59,
            value = timeInMinutes % 60,
            onValueChange = { onTimeChange(it + timeInMinutes - (timeInMinutes % 60)) },
        )
        ListItemPicker(
            modifier = Modifier.weight(1f),
            dividersColor = Color.Transparent,
            textStyle = TextStyle(
                fontSize = 36.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            value = if (isMorning) Constants.AM else Constants.PM,
            onValueChange = { dayPartChange(it == Constants.AM) },
            list = Constants.dayParts
        )
    }
}

@Composable
fun DatesPicker(
    selectedDays: Int,
    onDayChecked: (selectedDays: Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start
    ) {
        SelectedDaysText(selectedDays)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ){
            for (dayIndex in 0..6) {
                DayItem(
                    dayIndex = dayIndex,
                    isChecked = isDaySelected(selectedDays, dayIndex),
                    onCheck = { isChecked: Boolean ->
                        val selectedDay = (10 pow dayIndex) * (dayIndex + 1)
                        val newSelectedDays = if (isChecked)
                            selectedDays + selectedDay
                        else
                            selectedDays - selectedDay
                        onDayChecked(newSelectedDays)
                    }
                )
            }
        }

    }
}

@Composable
fun SelectedDaysText(
    selectedDays: Int
) {
    Text(
        text = getAlarmDaysText(selectedDays),
        modifier = Modifier
            .animateContentSize()
            .padding(start = 10.dp),
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun DayItem(
    dayIndex: Int,
    isChecked: Boolean,
    onCheck: (isChecked: Boolean) -> Unit
) {
    val circleRadius by animateFloatAsState(
        label = "",
        targetValue = if (isChecked) 20.dp.dpToPx() else 0f,
        animationSpec = tween(250)
    )
    val borderColor = MaterialTheme.colorScheme.primary
    IconButton(
        onClick = { onCheck(!isChecked) },
        modifier = Modifier
            .scale(0.7f)
            .drawBehind {
                drawCircle(
                    color = borderColor,
                    radius = circleRadius,
                    style = Stroke(2.dp.toPx())
                )
            }
    ) {
        Text(
            text = Constants.daysFirstLetterList[dayIndex].toString(),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun PreferenceItem(
    bottomLine: Boolean = true,
    name: String = "Preference Name",
    value: String = "Default value",
    isEnabled: Boolean = true,
    onSwitch: (isEnabled: Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {

    Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
        TextButton(
            onClick = { onClick() }
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = name,
                            fontSize = 20.sp
                        )
                        Text(
                            text = value,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier
                            .height(30.dp)
                            .width(1.dp)
                            .background(Color.LightGray)
                        )

                        Switch(
                            modifier = Modifier.padding(start = 10.dp),
                            checked = isEnabled,
                            onCheckedChange = { onSwitch(!isEnabled) }
                        )
                    }
                }
            }
        }
        if (bottomLine) MaxWidthHorizontalLine()
    }







}


@Composable
fun MaxWidthHorizontalLine() {
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(Color.LightGray))
}