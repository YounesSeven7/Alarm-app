package com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.alarm_screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.alarm_screen.components.AlarmItem
import com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.alarm_screen.components.AlarmTopAppBar

@Composable
fun AlarmScreen(
    navController: NavController,
    viewModel: AlarmViewModel = hiltViewModel()
) {

    val state = viewModel.alarmListState.collectAsState()

    val deletionModeState = viewModel.deletionModeState

    val selectedItemsCountState = viewModel.selectedItemsCountState



    val context = LocalContext.current

    val activity = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        viewModel.onEvent(AlarmScreenEvent.CreateAlarmNotificationChanel)
    }

    val addIconOnClick = remember{
        {
            if (checkPostPermission(context))
                viewModel.onEvent(AlarmScreenEvent.AddAlarm(navController))
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                activity.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }


    val editAlarm = remember<(Int) -> Unit> {
        { alarmIndex ->
            viewModel.onEvent(AlarmScreenEvent.EditAlarm(navController, alarmIndex))
        }
    }

    val onChangeEnabledState = remember<(Int, Boolean) -> Unit> {
        { alarmIndex, isEnabled ->
            viewModel.onEvent(AlarmScreenEvent.OnChangeEnabledState(alarmIndex, isEnabled))
        }
    }

    val onChangeDeletionState = remember<(Int, Boolean) -> Unit> {
        { alarmIndex, isSelectedToDelete ->
            viewModel.onEvent(
                AlarmScreenEvent.OnChangeDeletionState(
                    alarmIndex,
                    isSelectedToDelete
                )
            )
        }
    }

    val disableDeletionMode = remember {
        {
            deletionModeState.value = false
            viewModel.onEvent(AlarmScreenEvent.DisabledDeletionMode)
        }
    }

    BackHandler(enabled = deletionModeState.value) { disableDeletionMode() }

    val deleteAlarms = remember {
        {
            if (selectedItemsCountState.intValue == 0)
                Toast.makeText(context, "no Selected Alarm to delete", Toast.LENGTH_LONG).show()
            else
                viewModel.onEvent(AlarmScreenEvent.DeleteAlarms)
        }
    }



    Scaffold(
        topBar = {
            AlarmTopAppBar(
                addIconOnClick = addIconOnClick,
                editIconOnClick = { deletionModeState.value = true },
                deletingMode = deletionModeState.value,
                selectedItemsCount = selectedItemsCountState.intValue,
                closeIconOnClick = disableDeletionMode,
                deleteIconOnClick = deleteAlarms
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            itemsIndexed(state.value) {index, alarm ->
                AlarmItem(
                    alarm = alarm,
                    onChangeEnabledState = { isEnabled -> onChangeEnabledState(index, isEnabled) },
                    onClick = {
                        if (deletionModeState.value) onChangeDeletionState(index, !alarm.isSelectedToDelete)
                        else editAlarm(index)
                    },
                    deletingMode = deletionModeState.value
                )
            }
        }
    }
}

fun checkPostPermission(context: Context): Boolean {
 return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
     context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
             PackageManager.PERMISSION_GRANTED
 } else true
}