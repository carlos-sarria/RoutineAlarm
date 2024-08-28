package com.routinealarm.helpers

import android.util.Log
import android.widget.NumberPicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import com.routinealarm.textColor

enum class EditType {
    NONE, TEXT, NUMERIC, TIME, COMBO
}

@Composable
fun DialogWrapper(
    modifier : Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        modifier = modifier,
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
        properties = DialogProperties(usePlatformDefaultWidth = false),
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

@Composable
fun EditNumberDialog(
    label : String = "",
    text : String = "0",
    numRows : Int = 3,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
)
{
    if(numRows<1) return

    var n by remember { mutableStateOf(arrayOf<Int>()) }
    var exp  = 1
    for (i in 0..<numRows) {
        n += (text.toInt()/exp)%10 // separate each digit of the number into the array
        exp *= 10
        Log.i("NUM_1", i.toString()+" "+n[i].toString())
    }

    DialogWrapper(
        modifier = Modifier.width(200.dp),
        onDismiss = onDismiss,
        onConfirm = {
            var s : String = ""
            for (i in 0..<numRows) {
                s += n[i].toString()
                Log.i("NUM_3", i.toString()+" "+n[i].toString())
            } // recompose each digit into final string
            onConfirm(s)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                //modifier = Modifier.background(MaterialTheme.colorScheme.background),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in (numRows-1) downTo 0) {
                    Log.i("NUM", i.toString()+" "+n[i].toString())
                    SimpleNumberPicker(
                        value = n[i],
                        onValueChange = { n[i] = it;  Log.i("NUM_ACR", n[i].toString()) }
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleNumberPicker(
    value: Int,
    min: Int = 0,
    max: Int = 9,
    onValueChange: (Int) -> Unit
) {
    AndroidView(
        modifier = Modifier.width(20.dp),
        factory = { context ->
            NumberPicker(context).apply {
                setOnValueChangedListener { numberPicker, i, i2 -> onValueChange(i) }
                minValue = min
                maxValue = max
                this.value = value
                textSize = 100F
                clipToOutline = true
            }
        },
        update = {}
    )
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
fun ConfirmationBox(
    label : String = "",
    text : String = "",
    onConfirm :  () -> Unit = {},
    onDismiss :  () -> Unit = {},
) {
    var shouldDismiss by remember {mutableStateOf(false)}
    if (shouldDismiss) return

    AlertDialog(
        title = { Text(text = label) },
        text = {Text(style = MaterialTheme.typography.bodyLarge, text = text)},
        onDismissRequest = {shouldDismiss = true},
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EditBox(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    label: String = "",
    initialText: String = "",
    type: EditType = EditType.NONE,
    list: Array<String> = arrayOf(""),
    numRows: Int = 3,
    enabled : Boolean = true,
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
                colorFilter = ColorFilter.tint(textColor(enabled)),
                contentDescription = "collapsable"
            )
        }

        Text(
            modifier = modifier.padding(horizontal = 5.dp),
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor(enabled)
        )
        if(initialText != "") {
            Text(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(horizontal = 5.dp),
                text = text,
                color = textColor(enabled),
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
            EditNumberDialog(label, text, numRows,
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