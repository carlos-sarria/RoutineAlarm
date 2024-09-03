package com.routinealarm.helpers

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.routinealarm.R
import com.routinealarm.ui.theme.cBackgroundDialog
import com.routinealarm.ui.theme.cTextVariant

@Composable
fun DialogWrapper(
    modifier : Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.cBackgroundDialog,
        modifier = modifier,
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(color = MaterialTheme.colorScheme.cTextVariant,
                    text = stringResource(R.string.dismiss)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(color = MaterialTheme.colorScheme.cTextVariant,
                    text = stringResource(R.string.accept)
                )
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        text = { content() }
    )
}