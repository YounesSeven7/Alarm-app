package com.example.alarm_app.feature_alarm.presentation.screens.alarm_section.alarm_screen


import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.alarm_app.feature_alarm.domain.model.Alarm
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.repository.DeleteAlarms
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.repository.EditAlarm
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.repository.GetAllAlarms
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.schedule.CancelScheduledAlarm
import com.example.alarm_app.feature_alarm.domain.use_case.alarm_use_cases.schedule.ScheduleAlarm
import com.example.alarm_app.feature_alarm.util.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("NAME_SHADOWING")
@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val getAllAlarms: GetAllAlarms,
    private val editAlarm: EditAlarm,
    private val deleteAlarms: DeleteAlarms,
    private val scheduleAlarm: ScheduleAlarm,
    private val cancelScheduleAlarm: CancelScheduledAlarm
): ViewModel() {

    init {
        getAlarms()
    }

    private val _alarmListState = MutableStateFlow<List<Alarm>>(emptyList())
    val alarmListState: StateFlow<List<Alarm>> = _alarmListState

    val deletionModeState = mutableStateOf(false)

    val selectedItemsCountState = mutableIntStateOf( 0)


    private fun getAlarms() {
        viewModelScope.launch {
            getAllAlarms().collect { alarmsList ->
                _alarmListState.value = alarmsList
            }
        }
    }


    fun onEvent(event: AlarmScreenEvent) {
        when(event) {
            is AlarmScreenEvent.AddAlarm -> addAlarm(event.navController)
            is AlarmScreenEvent.EditAlarm -> editAlarm(event.navController, event.alarmIndex)
            is AlarmScreenEvent.OnChangeEnabledState -> changeEnabledState(event.alarmIndex, event.isEnabled)
            is AlarmScreenEvent.OnChangeDeletionState ->
                onChangeDeletionState(event.alarmIndex, event.isSelectedToDelete)
            is AlarmScreenEvent.DisabledDeletionMode -> makeDeletionStateOfAllAlarmsUnselect()
            is AlarmScreenEvent.DeleteAlarms -> deleteAlarmsList()
        }
    }




    private fun addAlarm(globalNavController: NavController) {
        globalNavController.navigate(Screen.AddEditAlarmScreen.route)
    }

    private fun editAlarm(globalNavController: NavController, alarmIndex: Int) {
        val alarmIDKey = Screen.AddEditAlarmScreen.Key!!
        val id = _alarmListState.value[alarmIndex].id!!
        globalNavController.navigate(
            Screen.AddEditAlarmScreen.route + "?$alarmIDKey=$id"
        )
    }

    private fun changeEnabledState(alarmIndex: Int, isEnabled: Boolean) {
        var alarm = _alarmListState.value[alarmIndex]
        alarm = alarm.copy(isEnabled = isEnabled)
        viewModelScope.launch { editAlarm(alarm) }
        if (isEnabled) scheduleAlarm(alarm) else cancelScheduleAlarm(alarm)
    }




    private fun onChangeDeletionState(alarmIndex: Int, isSelectedToDelete: Boolean) {
        _alarmListState.value = _alarmListState.value.mapIndexed { index, alarm ->
            if (index == alarmIndex) alarm.copy(isSelectedToDelete = isSelectedToDelete)
            else alarm
        }

        selectedItemsCountState.intValue += if (isSelectedToDelete) 1 else -1
    }

    private fun deleteAlarmsList() {
        viewModelScope.launch {
            val selectedToDeleteAlarms = _alarmListState.value.filter {
                alarm ->
                cancelScheduleAlarm(alarm)
                alarm.isSelectedToDelete
            }
            deleteAlarms(selectedToDeleteAlarms)
            selectedItemsCountState.intValue = 0
            deletionModeState.value = false
        }
    }

    private fun makeDeletionStateOfAllAlarmsUnselect() {
        _alarmListState.value = _alarmListState.value.map { alarm ->
            if (alarm.isSelectedToDelete) alarm.copy(isSelectedToDelete = false)
            else alarm
        }
        selectedItemsCountState.intValue = 0
    }




}
