package com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.alarm_screen.components

import android.view.animation.ScaleAnimation
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmTopAppBar(
    addIconOnClick: () -> Unit,
    editIconOnClick: () -> Unit,
    deletingMode: Boolean,
    selectedItemsCount: Int,
    closeIconOnClick: () -> Unit,
    deleteIconOnClick: () -> Unit
) {
    TopAppBar(
        title = { Title(deletingMode, selectedItemsCount) },
        actions = {

            if (deletingMode) {
                CloseIcon(closeIconOnClick = closeIconOnClick)
                DeleteIcon(deleteIconOnClick = deleteIconOnClick)
            } else {
                AddIcon(addIconOnClick = addIconOnClick, deletingMode = deletingMode)
                EditIcon(editIconOnClick = editIconOnClick)
            }

        }
    )
}

@Composable
fun DeleteIcon(deleteIconOnClick: () -> Unit) {
    IconButton(onClick = deleteIconOnClick) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun CloseIcon(
    closeIconOnClick: () -> Unit
) {
    IconButton(onClick = closeIconOnClick) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun EditIcon(
    editIconOnClick: () -> Unit
) {
    IconButton(onClick = { editIconOnClick() }) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}


@Composable
fun Title(
    deletingMode: Boolean,
    selectedItemsCount: Int,
) {
    val text = if (deletingMode && selectedItemsCount == 0) "Select alarms"
        else if (deletingMode) "$selectedItemsCount selected"
        else "Alarm"
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun AddIcon(
    addIconOnClick: () -> Unit,
    deletingMode: Boolean
) {
    val scaleAnimation by animateFloatAsState(
        targetValue = if(deletingMode) 0f else 1f,
        label = "",
        animationSpec = tween(durationMillis = 300)
    )

    IconButton(onClick = addIconOnClick) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(32.dp).scale(scaleAnimation),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}
