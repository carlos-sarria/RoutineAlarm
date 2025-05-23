package com.routinealarm

import android.provider.Settings.Global.getString
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.routinealarm.helpers.AppInfo
import com.routinealarm.helpers.ConfirmationDialog
import com.routinealarm.ui.theme.cBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmList(
    model: ViewModel,
){
    val scope = currentRecomposeScope
    var showMenu by remember { mutableStateOf(false) }
    var expandedId by  remember { mutableIntStateOf(-1) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var deleteAll by remember { mutableStateOf(false) }
    var showInfo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 8.dp),
                title = {
                    Row()
                    {
                        Text(text = stringResource(R.string.app_name))
                    }
                        },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(text = { Text(text = stringResource(R.string.sort_by_label)) },
                            onClick = {model.sort(useTime=false); scope.invalidate()})
                        DropdownMenuItem(text = { Text(text = stringResource(R.string.sort_by_time)) },
                            onClick = {model.sort(useTime=true); scope.invalidate()})
                        DropdownMenuItem(text = { Text(text = stringResource(R.string.delete_all)) },
                            onClick = {deleteAll = true})
                        DropdownMenuItem(text = { Text(text = stringResource(R.string.info)) },
                            onClick = {showInfo = true})
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                containerColor = cBackground,
                shape = CircleShape,
                onClick = {
                    expandedId = model.add().id
                    scope.invalidate()
                    model.saveAlarms()
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
        if(deleteAll) {
            showMenu = false
            ConfirmationDialog(
                label = stringResource(R.string.delete_all),
                text = stringResource(R.string.delete_all_alarms),
                onConfirm = { model.deleteChecked(forceAll = true); scope.invalidate(); model.saveAlarms(); deleteAll = false },
                onDismiss = {deleteAll = false}
            )
        }
        if(showInfo) {
            showMenu = false
            AppInfo(
                onConfirm = { showInfo = false },
                onDismiss = { showInfo = false }
            )
        }
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            state = listState
        ) {
            items(
                items = model.alarms,
                //key = { alarm: Alarm -> alarm.id }
            ) { alarm: Alarm ->
                AlarmItem(
                    alarm = alarm,
                    forceExpanded = (alarm.id==expandedId),
                    onEnableChanged = { enabled -> model.changeAlarmEnabled(alarm, enabled) },
                    onDeleted = { alarm.checked = true; model.deleteChecked(); scope.invalidate()},
                    onUpdated = { model.setSystemAlarm(alarm); model.saveAlarms() }
                )
            }
        }
    }
}
