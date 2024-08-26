package com.routinealarm.helpers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector

enum class EditType {
    NONE, TEXT, NUMERIC, TIME, COMBO
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

@Composable
fun EditTextDialog(
    label : String = "",
    text : String = "",
    isNumeric : Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
)
{
    var editText by rememberSaveable { mutableStateOf(text) }

    DialogWrapper( // From TimeSelect
    onDismiss = onDismiss,
    onConfirm = {onConfirm(editText)}
    ) {
        OutlinedTextField(
            value = editText,
            singleLine = true,
            onValueChange = { editText = it },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isNumeric) KeyboardType.Number else KeyboardType.Text
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTimeDialog(
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

@Composable
fun EditComboDialog(
    list: Array<String>,
    selected : String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
)
{
    var selectedText by remember { mutableStateOf(selected) }
    var expanded by remember { mutableStateOf(true) }

   DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false; onDismiss() }
        )
   {
        list.forEach { item ->
            DropdownMenuItem(
                text = { Text(text = item) },
                onClick = {
                    selectedText = item
                    onConfirm(selectedText)
                }
            )
        }
    }
}

@Composable
fun EditBox(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    label: String = "",
    initialText: String = "",
    type: EditType = EditType.NONE,
    list: Array<String> = arrayOf(""),
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var openDialog by remember { mutableStateOf(false) }
    var text by rememberSaveable { mutableStateOf(initialText) }

    Row(modifier = modifier.clickable {
        if(type == EditType.NONE) onConfirm("")
        else openDialog = true })
    {
        if (icon != null) {
            Image(
                modifier = modifier.size(24.dp),
                imageVector = icon,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimaryContainer),
                contentDescription = "collapsable"
            )
        }

        Text(
            modifier = modifier.padding(horizontal = 5.dp),
            text = label,
            style = MaterialTheme.typography.bodyLarge,
        )
        if(initialText != "") {
            Text(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(horizontal = 5.dp),
                text = text,
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    if (openDialog) {
        if (type == EditType.TEXT) {
            EditTextDialog(label, text, false,
                { openDialog = false; onDismiss() },
                { openDialog = false; text = it; onConfirm(text) }
            )
        }
        if (type == EditType.NUMERIC) {
            EditTextDialog(label, text, true,
                { openDialog = false; onDismiss() },
                { openDialog = false; text = it; onConfirm(text) }
            )
        }
        if (type == EditType.TIME) {
            EditTimeDialog(text,
                { openDialog = false; onDismiss() },
                { openDialog = false; text = it; onConfirm(text) }
            )
        }
        if (type == EditType.COMBO) {
            EditComboDialog(
                list, text,
                { openDialog = false; onDismiss() },
                { openDialog = false; text = it; onConfirm(text) }
            )
        }
    }
}