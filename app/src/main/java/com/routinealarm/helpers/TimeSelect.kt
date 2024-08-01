@file:OptIn(ExperimentalMaterial3Api::class)

package com.routinealarm

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelect(
    title : String = "",
    enabled : Boolean = true,
    currentTime : String = "00:00",
    onChange : (String) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }
    var timeString by remember { mutableStateOf(currentTime) }

    // ExposedDropdownMenuBox give us a clickable box for
    // TextField (which we use just for the style)
    ExposedDropdownMenuBox(expanded = false, onExpandedChange = { if(enabled) openDialog = true})
    {
        OutlinedTextField(
            enabled = enabled,
            value = timeString,
            onValueChange = {},
            readOnly = true,
            label = { Text(title) },
            trailingIcon = { },
            modifier = Modifier.menuAnchor()
        )
    }

    if (openDialog) {
        GetTime(
            onConfirm = {
                timePickerState ->
                timeString = timePickerState.hour.toString().padStart(2, '0')+":"+
                             timePickerState.minute.toString().padStart(2, '0')
                openDialog = false
                onChange(timeString)
            },
            onDismiss = { openDialog = false },
            currentTime = timeString
        )
    }
}

@Composable
fun DialogWrapper(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetTime(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
    currentTime : String
) {
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.substring(0, 2).toInt(),
        initialMinute = currentTime.substring(3).toInt(),
        is24Hour = true,
    )

    DialogWrapper(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) }
    ) {
        TimePicker(state = timePickerState)
    }
}
