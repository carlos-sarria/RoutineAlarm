package com.routinealarm.helpers

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun TextDialog(
    label : String = "",
    text : String = "",
    isNumeric : Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
)
{
    var editText by rememberSaveable { mutableStateOf(text) }

    DialogWrapper(
        onDismiss = onDismiss,
        onConfirm = {onConfirm(editText)}
    ) {
        OutlinedTextField(
            value = editText,
            singleLine = true,
            onValueChange = { editText = it; },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isNumeric) KeyboardType.Number else KeyboardType.Text
            )
        )
    }
}
