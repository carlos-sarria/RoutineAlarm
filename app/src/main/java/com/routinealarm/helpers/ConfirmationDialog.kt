package com.routinealarm.helpers

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun ConfirmationDialog(
    label : String = "",
    text : String = "",
    onConfirm :  () -> Unit = {},
    onDismiss :  () -> Unit = {},
) {
    var shouldDismiss by remember { mutableStateOf(false) }
    if (shouldDismiss) return

    AlertDialog(
        title = { Text(text = label) },
        text = { Text(style = MaterialTheme.typography.bodyLarge, text = text) },
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