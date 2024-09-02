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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.routinealarm.helpers.EditBox
import com.routinealarm.helpers.EditType
import com.routinealarm.ui.theme.cBackground
import com.routinealarm.ui.theme.cBackgroundDialog
import com.routinealarm.ui.theme.cTextDisabled


@Composable
fun Separator () {
    Box(
        modifier = Modifier.fillMaxWidth().height(1.dp)
            .background(MaterialTheme.colorScheme.cTextDisabled)
    )
}

@Composable
fun RoundButton(
    text: String = "",
    selected : Boolean = true,
    enabled : Boolean = true,
    onClick: () -> Unit
)
{
    val buttonColor : Color = if (selected) MaterialTheme.colorScheme.cBackground
                        else MaterialTheme.colorScheme.cBackgroundDialog

    Box(Modifier.padding(2.dp))
    {
        Button(
            modifier = Modifier.size(40.dp, 40.dp),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            contentPadding = ButtonDefaults.TextButtonContentPadding,
            shape = RoundedCornerShape(50)
        ) {
            Text(
                color = textColor(enabled),
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Visible,
                style = MaterialTheme.typography.bodyLarge,
                text = text.first().toString()
            )
        }
    }
}

@Composable
fun AlarmSetup (
    modifier: Modifier = Modifier,
    alarm: Alarm,
    enabled : Boolean = false,
    onDelete: () -> Unit,
    onUpdated: () -> Unit
    )
{
        val scope = currentRecomposeScope

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start)
        {
            EditBox(
                icon = R.drawable.label,
                label = "Label", initialText = alarm.label, type = EditType.TEXT,
                enabled = enabled,
                onConfirm = { text: String -> alarm.label = text; onUpdated() },
                onDismiss = {})

            EditBox(
                icon = R.drawable.alarm,
                label = "Start Time", initialText = alarm.timeStart, type = EditType.TIME,
                enabled = enabled,
                onConfirm = { text: String -> alarm.timeStart = text; onUpdated() },
                onDismiss = {})
            Row()
            {
                EditBox(
                    icon = R.drawable.replay,
                    label = "Interval",
                    initialText = alarm.timeInterval,
                    type = EditType.NUMERIC,
                    numRows = 3,
                    enabled = enabled,
                    onConfirm = { text: String -> alarm.timeInterval = text; onUpdated() },
                    onDismiss = {})

                EditBox(
                    label = "Reps",
                    initialText = alarm.numIntervals,
                    type = EditType.NUMERIC,
                    numRows = 2,
                    enabled = enabled,
                    onConfirm = { text: String -> alarm.numIntervals = text; onUpdated() },
                    onDismiss = {})
            }
            Row()
            {
                EditBox(
                    icon = R.drawable.notifications,
                    label = "Sound",
                    initialText = alarm.soundName,
                    type = EditType.COMBO,
                    list = soundList,
                    enabled = enabled,
                    onConfirm = { text: String -> alarm.soundName = text; onUpdated() },
                    onDismiss = {})
                EditBox(
                    label = "Reps",
                    initialText = alarm.soundRep,
                    type = EditType.NUMERIC,
                    numRows = 1,
                    enabled = enabled,
                    onConfirm = { text: String -> alarm.soundRep = text; onUpdated() },
                    onDismiss = {})
            }

            val weekdays : Array<String> = arrayOf("M", "T", "W", "T", "F", "S", "S")
            Row()
            {
                weekdays.forEachIndexed { i, item ->
                    RoundButton (text = item,
                        selected = alarm.weeklyRep[i],
                        enabled  = enabled,
                        onClick = {
                            alarm.weeklyRep[i] = !alarm.weeklyRep[i]
                            scope.invalidate()
                            onUpdated()
                        }
                    )
                }
            }

            Separator ()

            EditBox(
                modifier = modifier.padding(top = 2.dp, bottom = 5.dp),
                icon = R.drawable.delete,
                label = "Delete",
                type = EditType.NONE,
                enabled = enabled,
                onConfirm = {onDelete()},
                onDismiss = {})
        }
}
