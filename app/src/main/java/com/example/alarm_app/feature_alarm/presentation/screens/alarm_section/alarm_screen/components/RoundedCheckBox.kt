package com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.alarm_screen.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



@Composable
fun RoundedCheckBox(
    modifier: Modifier,
    isSelected: Boolean,
    color: Color = MaterialTheme.colorScheme.primary
) {

    val scaleAnimation by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        label = "",
        animationSpec = tween(durationMillis = 200)
    )

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color)
            .padding(1.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "",
            modifier = Modifier.scale(scaleAnimation),
            tint = color
        )
    }
}