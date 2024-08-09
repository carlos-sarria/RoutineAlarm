package com.routinealarm

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.routinealarm.MainActivity.Companion.appContext
import com.routinealarm.helpers.ComboBox
import com.routinealarm.helpers.LineEdit
import com.routinealarm.helpers.MultipleSelection
import com.routinealarm.helpers.TimeSelect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmSetup (
    modifier: Modifier = Modifier,
    model : ViewModel,
    alarmId: Int = 0,
    onClick: (Boolean) -> Unit
    )
{
    // temporal alarm holder, to be copied on accept
    val alarm by remember { mutableStateOf(
        if(alarmId == -1)  Alarm(model.alarms.size)
        else model.alarms[alarmId]
    ) }

    Column (modifier = modifier.padding(start = 10.dp, top = 50.dp, end = 10.dp, bottom = 50.dp))
    {
        // Header with Cancel and Accept buttons
        Row(modifier = modifier.padding(horizontal = 20.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically)
        {
            Icon(Icons.Outlined.Close,
                contentDescription = "Cancel",
                modifier = modifier.clickable { onClick(false) })

            Text(
                modifier = modifier
                    .padding(horizontal = 5.dp)
                    .weight(1f),
                text = "Set Alarm",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
            )

            val time = rememberTimePickerState(initialHour = 12, initialMinute = 28, is24Hour = true)

            Icon(Icons.Outlined.Check,
                contentDescription = "Accept",
                modifier = modifier.clickable {
                    if(alarmId==-1) model.add(alarm)
                    else {
                        model.removeSystemAlarm(model.alarms[alarmId])  // delete previous alarms
                        model.copy(alarm, model.alarms[alarmId])
                    }
                    model.setSystemAlarm(alarm) // set new
                    model.saveAlarms()
                    onClick(true)

                }
            )
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            LineEdit(title = "Name", initialText = alarm.label,
                onChange = { text: String -> alarm.label = text })

            TimeSelect(
                title = "Time",
                currentTime = alarm.timeStart,
                onChange = { time: String -> alarm.timeStart = time })

            LineEdit(
                title = "Number of Intervals",
                initialText = if (alarm.numIntervals=="") "0" else alarm.numIntervals,
                isNumeric = true,
                onChange = { text: String -> alarm.numIntervals = text })

            LineEdit(
                title = "Time Interval (minutes)",
                initialText = if (alarm.timeInterval=="") "0" else alarm.timeInterval,
                isNumeric = true,
                onChange = { text: String -> alarm.timeInterval = text })

            ComboBox(
                title = "Sound",
                list = soundList,
                onChange = { sound: String -> alarm.soundName = sound })

            LineEdit(
                title = "Sound Repetition",
                initialText = if (alarm.soundRep=="") "0" else alarm.soundRep,
                isNumeric = true,
                maximum = 20,
                onChange = { text: String -> alarm.soundRep = text })

            MultipleSelection(
                title = "Day Schedule",
                listNames = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),
                listStates = alarm.weeklyRep,
                onChange = { list -> alarm.weeklyRep.copyInto(list)}
            )
        }

    }
}
