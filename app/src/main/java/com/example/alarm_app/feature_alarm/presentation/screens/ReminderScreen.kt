package com.example.alarm_app.feature_alarm.presentation.screens



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Preview(apiLevel = 33)
@Composable
fun ReminderScreen(
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 60.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DateTimeSection()
            Button(
                modifier = Modifier.scale(2f),
                onClick = onClick
            ) {
                Text(text = "stop")
            }
        }
    }
}

@Composable
fun DateTimeSection() {
    val now = LocalDateTime.now()

    val time = "${now.hour}:${now.minute}"

    val dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM d")
    val date = now.format(dateFormatter).toString()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.displayLarge,
        )
        Text(text = date)
        Text(
            text = "Alarm",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 50.dp)
        )
    }
}