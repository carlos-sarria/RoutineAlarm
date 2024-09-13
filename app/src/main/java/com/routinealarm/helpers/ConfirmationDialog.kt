package com.routinealarm.helpers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.routinealarm.ui.theme.cBackgroundDialog
import com.routinealarm.ui.theme.cTextVariant

@Composable
fun ConfirmationDialog(
    label : String = "",
    text : String = "",
    onConfirm :  () -> Unit = {},
    onDismiss :  () -> Unit = {},
) {
    var shouldDismiss by remember { mutableStateOf(false) }
    if (shouldDismiss) return

    DialogWrapper(
        onDismiss = onDismiss,
        onConfirm = onConfirm
    ) {
        Column()
        {
            Text(style = MaterialTheme.typography.titleMedium, text = label)
            Text(modifier = Modifier.padding(top = 30.dp), style = MaterialTheme.typography.bodyMedium, text = text)
        }
    }
}