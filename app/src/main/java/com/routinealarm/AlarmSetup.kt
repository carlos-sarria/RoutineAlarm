package com.routinealarm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.routinealarm.helpers.EditBox
import com.routinealarm.helpers.EditType


@Composable
fun Separator () {
    Box(
        modifier = Modifier.fillMaxWidth().height(1.dp)
            .background(MaterialTheme.colorScheme.inversePrimary)
    )
}

@Composable
fun AlarmSetup (
    modifier: Modifier = Modifier,
    alarm: Alarm,
    onDelete: () -> Unit,
    onUpdated: () -> Unit
    )
{
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start)
        {
            EditBox(
                icon = Icons.Outlined.Create,
                label = "Label", initialText = alarm.label, type = EditType.TEXT,
                onConfirm = { text: String -> alarm.label = text; onUpdated() },
                onDismiss = {})

            EditBox(
                icon = Icons.Outlined.DateRange,
                label = "Start Time", initialText = alarm.timeStart, type = EditType.TIME,
                onConfirm = { text: String -> alarm.timeStart = text; onUpdated() },
                onDismiss = {})
            Row()
            {
                EditBox(
                    icon = Icons.Outlined.Refresh,
                    label = "Interval (minutes)",
                    initialText = alarm.timeInterval,
                    type = EditType.NUMERIC,
                    onConfirm = { text: String -> alarm.timeInterval = text; onUpdated() },
                    onDismiss = {})

                EditBox(
                    label = "Reps",
                    initialText = alarm.numIntervals,
                    type = EditType.NUMERIC,
                    onConfirm = { text: String -> alarm.numIntervals = text; onUpdated() },
                    onDismiss = {})
            }
            Row()
            {
                EditBox(
                    icon = Icons.Outlined.Notifications,
                    label = "Sound",
                    initialText = alarm.soundName,
                    type = EditType.COMBO,
                    list = soundList,
                    onConfirm = { text: String -> alarm.soundName = text; onUpdated() },
                    onDismiss = {})

                EditBox(
                    label = "Reps",
                    initialText = alarm.soundRep,
                    type = EditType.NUMERIC,
                    onConfirm = { text: String -> alarm.soundRep = text; onUpdated() },
                    onDismiss = {})
            }

            Separator ()

            EditBox(
                modifier = modifier.padding(top = 2.dp, bottom = 5.dp),
                icon = Icons.Outlined.Delete,
                label = "Delete",
                type = EditType.NONE,
                onConfirm = {onDelete()},
                onDismiss = {})
        }
}
