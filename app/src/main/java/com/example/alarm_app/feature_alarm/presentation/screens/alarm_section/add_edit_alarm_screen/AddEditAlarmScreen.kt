package com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.add_edit_alarm_screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.add_edit_alarm_screen.components.DatesPicker
import com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.add_edit_alarm_screen.components.PreferenceItem
import com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.add_edit_alarm_screen.components.TimePicker
import com.example.alarm_app.feature_alarm.util.Constants


@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AddEditAlarmScreen(
    navController: NavController,
    viewModel: AddEditAlarmViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val timeInMinutes = viewModel.timeInMinutes
    val isMorning = viewModel.isMorning

    val selectedDats = viewModel.selectedDats

    val labelValue = viewModel.labelValue

    val isAlarmSoundEnabled = viewModel.isAlarmSoundEnabled
    val alarmSoundName = viewModel.alarmSoundName

    val isVibrationEnabled = viewModel.isVibrationEnabled

    val addAlarmLambda = remember<(Context) -> Unit> {
        {
            context -> viewModel.addUpdateAlarm(context, navController)
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
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(start = it.calculateTopPadding())
        ) {
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
                        onClick = {
                            startRingtonePickerActivity(
                                viewModel.alarmSoundUri,
                                ringtoneActivityForResult
                            )
                        }
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
                    onClick = {
                        navController.popBackStack()
                    }
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

fun startRingtonePickerActivity(
    soundUri: String?,
    ringtoneActivityForResult: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
        putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
        putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
        putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        soundUri?.let { putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, soundUri.toUri()) }
        ringtoneActivityForResult.launch(this)
    }
}


fun getDefaultRingtoneName(context: Context) =
    getRingtoneName(context, Settings.System.DEFAULT_ALARM_ALERT_URI)

fun getRingtoneName(context: Context, uri: Uri): String =
    RingtoneManager.getRingtone(context, uri).getTitle(context)


