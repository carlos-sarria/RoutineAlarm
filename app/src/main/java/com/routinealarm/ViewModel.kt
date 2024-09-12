package com.routinealarm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.routinealarm.helpers.Prefs
import kotlin.Int.Companion.MAX_VALUE
import kotlin.random.Random
import com.routinealarm.GlobalData.Companion.appContext

data class Alarm (var id: Int)
{
    var checked by mutableStateOf(false)
    var enabled by mutableStateOf(true)
    var label by mutableStateOf(appContext.getString(R.string.alarm_label_default).format(id))
    var timeStart by mutableStateOf("08:00")
    var timeInterval by mutableStateOf("000")
    var numIntervals by mutableStateOf("00")
    var soundName by mutableStateOf(appContext.getString(R.string.blip))
    var soundRep by mutableStateOf("1")
    var requestCode by mutableIntStateOf(Random.nextInt(0, MAX_VALUE))
    var weeklyRep by mutableStateOf(arrayOf(true, true, true, true, true, true,true))
}

fun hour(string : String) : Int = string.substring(0, 2).toInt()
fun minute(string : String) : Int = string.substring(3, 5).toInt()

class ViewModel : ViewModel() {

    private var _alarms : MutableList<Alarm> = listOf<Alarm>().toMutableList() // Empty List

    init {
        loadAlarms()
        _alarms.forEach { alarm -> setSystemAlarm(alarm); } // Set all alarms at start-up
    }

    val alarms: List<Alarm> // This is to been accessed from outside
        get() = _alarms // Implement get() for val

    fun add() : Alarm {
        val newAlarm = Alarm(_alarms.size+1)
        _alarms.add(newAlarm)
        return newAlarm
    }

    fun sort(useTime : Boolean = false) {
        _alarms = _alarms.sortedBy {if(useTime) it.timeStart else it.label}.toMutableList()
    }

    fun deleteChecked(forceAll : Boolean = false) {
        val iterator = _alarms.iterator()
        while (iterator.hasNext()) { // Safe item deletion from mutable list
            val element = iterator.next()
            if (element.checked || forceAll) {
                iterator.remove()
                ScheduleNotification.clear(element.requestCode)
            }
        }
        saveAlarms()
        if(forceAll) ScheduleNotification.clearAll()
    }

    fun changeAlarmEnabled(item: Alarm, checked: Boolean) =
        _alarms.find { it.id == item.id }?.let { alarm ->
            alarm.enabled = checked
            if(alarm.enabled) setSystemAlarm(alarm) else removeSystemAlarm(alarm)
            saveAlarms()
        }

    private fun removeSystemAlarm(alarm : Alarm)
    {
        ScheduleNotification.clear(alarm.requestCode)
    }

    fun setSystemAlarm(alarm : Alarm)
    {
        val hour = hour(alarm.timeStart)
        val minute = minute(alarm.timeStart)
        ScheduleNotification.schedule(
            hour = hour,
            minute = minute,
            requestCode = alarm.requestCode,
        )
    }

    fun saveAlarms(){
        Prefs.clear()
        Prefs.set(key="Num", value=alarms.size)
        _alarms.forEachIndexed { index, alarm ->
            Prefs.set(key="enabled_$index", value=alarm.enabled)
            Prefs.set(key="label_$index", value=alarm.label)
            Prefs.set(key="timeStart_$index", value=alarm.timeStart)
            Prefs.set(key="numIntervals_$index", value=alarm.numIntervals)
            Prefs.set(key="timeInterval_$index", value=alarm.timeInterval)
            Prefs.set(key="soundName_$index", value=alarm.soundName)
            Prefs.set(key="requestCode_$index", value=alarm.requestCode)
            Prefs.set(key="soundRep_$index", value=alarm.soundRep)
            for (i in 0..6)
                Prefs.set(key="weeklyRep_$index-$i", value=alarm.weeklyRep[i])
        }
    }

    private fun loadAlarms() {
        val numAlarms : Int = Prefs.get(key="Num")
        for (id in 0..< numAlarms ) {
            val alarm  = Alarm(id)
            alarm.checked = false
            alarm.enabled = Prefs.get(key="enabled_$id")
            alarm.label = Prefs.get(key="label_$id")
            alarm.timeStart = Prefs.get(key="timeStart_$id")
            alarm.numIntervals = Prefs.get(key="numIntervals_$id")
            alarm.timeInterval = Prefs.get(key="timeInterval_$id")
            alarm.soundName = Prefs.get(key="soundName_$id")
            alarm.requestCode = Prefs.get(key="requestCode_$id")
            alarm.soundRep = Prefs.get(key="soundRep_$id")
            for (i in 0..6)
                alarm.weeklyRep[i] = Prefs.get(key="weeklyRep_$id-$i")
            _alarms.add(alarm)
        }
    }
}

