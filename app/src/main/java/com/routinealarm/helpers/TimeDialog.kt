package com.routinealarm.helpers

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDialog(
    currentTime : String = "00:00",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
)
{
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.substring(0, 2).toInt(),
        initialMinute = currentTime.substring(3).toInt(),
        is24Hour = true,
    )

    DialogWrapper(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(
            timePickerState.hour.toString().padStart(2, '0')+":"+
                    timePickerState.minute.toString().padStart(2, '0')) }
    ) {
        TimePicker(state = timePickerState)
    }
}