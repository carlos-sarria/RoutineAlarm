package com.routinealarm.helpers

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties

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
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(color = MaterialTheme.colorScheme.onSurface,
                    text = "Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(color = MaterialTheme.colorScheme.onSurface,
                    text = "OK")
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        text = { content() }
    )
}