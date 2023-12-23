package com.example.alarm_app.feature_alarm.presentation.screens


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ReminderScreen(
    onClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = onClick
        ) {
            Text(text = "stop Alarm")
        }
    }
}