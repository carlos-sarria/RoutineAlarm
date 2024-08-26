package com.routinealarm

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.routinealarm.helpers.DialogWrapper
import com.routinealarm.helpers.EditTimeDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmList(
    modifier: Modifier = Modifier,
    model: ViewModel,
    alarmList : List<Alarm>
){
    val scope = currentRecomposeScope
    var showMenu by remember { mutableStateOf(false) }
    var expanded_id by  remember { mutableStateOf(-1) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 8.dp),
                title = { Text(text = "Routine Alarm") },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(text = { Text(text = "Sort by Label") }, onClick = {})
                        DropdownMenuItem(text = { Text(text = "Sort by Time") }, onClick = {})
                        DropdownMenuItem(text = { Text(text = "Clear all") }, onClick = {
                            model.deleteChecked(forceAll = true)
                            scope.invalidate()
                        })
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
                onClick = {
                    expanded_id = model.add().id
                    scope.invalidate()
                    coroutineScope.launch {
                        listState.animateScrollToItem(model.alarms.size)
                    }
                },
                modifier = Modifier,
            ) {
                Icon(Icons.Outlined.Add, null)
            }
        }

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            state = listState
        ) {
            items(
                items = alarmList,
                key = { alarm: Alarm -> alarm.id }
            ) { alarm: Alarm ->
                AlarmItem(
                    alarm = alarm,
                    forceExpanded = (alarm.id==expanded_id),
                    enabled = alarm.enabled,
                    onEnabled = { enabled -> model.changeAlarmEnabled(alarm, enabled) },
                    onDeleted = { alarm.checked = true; model.deleteChecked();scope.invalidate()},
                )
            }
        }
    }
}
