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
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier,
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(color = MaterialTheme.colorScheme.onTertiary,
                    text = "Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(color = MaterialTheme.colorScheme.onTertiary,
                    text = "OK")
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        text = { content() }
    )
}