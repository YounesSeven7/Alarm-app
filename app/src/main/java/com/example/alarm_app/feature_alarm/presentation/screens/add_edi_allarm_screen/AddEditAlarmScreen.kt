package com.example.alarm_app.feature_alarm.presentation.screens.add_edi_allarm_screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold

import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import com.example.alarm_app.feature_alarm.presentation.util.Constants
import com.example.alarm_app.feature_alarm.presentation.util.Screen

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AddEditAlarmScreen(
    context: Context,
    navController: NavController,
    viewModel: AddEditAlarmViewModel = hiltViewModel()
) {
    var timeInMinutes = viewModel.timeInMinutes
    var isMorning = viewModel.isMorning

    var selectedDats = viewModel.selectedDats

    var labelValue = viewModel.labelValue

    var isAlarmSoundEnabled = viewModel.isAlarmSoundEnabled
    var alarmSoundName = viewModel.alarmSoundName

    var isVibrationEnabled = viewModel.isVibrationEnabled

    val addAlarmLambda = remember<(Context) -> Unit> {
        { context ->
            viewModel.addUpdateAlarm(context) {
                navController.popBackStack()
            }
        }
    }


    val ringtoneActivityForResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        it.data?.let { intent ->
            val uri = getRingtoneUri(intent)
            viewModel.alarmSoundUri = uri.toString()
            val ringtone = RingtoneManager.getRingtone(context, uri)
            alarmSoundName.value = ringtone.getTitle(context)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(start = it.calculateTopPadding())) {
            TimePicker(
                timeInMinutes = timeInMinutes.intValue,
                isMorning = isMorning.value,
                onTimeChange = { timeInMinutes.intValue = it },
                dayPartChange =  { isMorning.value = it }
            )
            Card {
                Column(
                    Modifier.padding(vertical = 10.dp, horizontal = 16.dp)
                ) {
                    DatesPicker(
                        selectedDays = selectedDats.intValue,
                        onDayChecked = { selectedDats.intValue = it  }
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        value = labelValue.value,
                        onValueChange = { labelValue.value = it },
                        label = { Text("Label") },
                        singleLine = true
                    )

                    PreferenceItem(
                        name = "Alarm sound",
                        value = alarmSoundName.value,
                        isEnabled = isAlarmSoundEnabled.value,
                        onSwitch = { isAlarmSoundEnabled.value = it },
                        onClick = { startRingtonePickerActivity(ringtoneActivityForResult) }
                    )
                    PreferenceItem(
                        name = "Vibration",
                        value = if (isVibrationEnabled.value) Constants.ON else Constants.OFF,
                        isEnabled = isVibrationEnabled.value,
                        onSwitch = { isVibrationEnabled.value= it }
                    )

                }

            }

            Row {
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = { navController.popBackStack() }
                ) {
                    Text(text = "Cancel")
                }

                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = { addAlarmLambda(context) }
                ) {
                    Text(text = "Save")
                }
            }

        }
    }
}


@Suppress("DEPRECATION")
private fun getRingtoneUri(intent: Intent) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, Uri::class.java)
    } else {
        intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
    }

fun startRingtonePickerActivity(ringtoneActivityForResult: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
        putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
        putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
        putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        ringtoneActivityForResult.launch(this)
    }
}


fun getDefaultRingtoneName(context: Context) =
    getRingtoneName(context, Settings.System.DEFAULT_ALARM_ALERT_URI)

fun getRingtoneName(context: Context, uri: Uri): String =
    RingtoneManager.getRingtone(context, uri).getTitle(context)
