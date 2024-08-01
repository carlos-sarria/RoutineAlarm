package com.routinealarm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class Alarm (var id: Int)
{
    var checked by mutableStateOf(false)
    var enabled by mutableStateOf(true)
    var label by mutableStateOf("Alarm_%03d".format(id))
    var timeStart by mutableStateOf("08:00")
    var timeEnd by mutableStateOf("12:00")
    var timeInterval by mutableStateOf("0")
    var doTimeInterval by mutableStateOf(false)
    var soundName by mutableStateOf("chime")
    var soundRep by mutableStateOf("1")
    var weeklyRep by mutableStateOf(arrayOf(false, false, false, false, false, false,false))

}

var soundList = arrayOf("chime", "bell", "blip", "bong", "drum")

class ViewModel : ViewModel() {

    private var _alarms : MutableList<Alarm> = listOf<Alarm>().toMutableList() // Empty List

    init {
        loadAlarms()
    }

    val alarms: List<Alarm> // This is to been accessed from outside
        get() = _alarms // Implement get() for val

    fun add(alarm : Alarm) {
        _alarms.add(alarm)
        alarmsUpdated()
    }

    fun copy(source : Alarm, destination : Alarm) {
        destination.checked = source.checked
        destination.enabled  = source.enabled
        destination.label = source.label
        destination.timeStart = source.timeStart
        destination.timeEnd = source.timeEnd
        destination.timeInterval = source.timeInterval
        destination.soundName = source.soundName
        destination.soundRep = source.soundRep
        destination.weeklyRep.copyInto(source.weeklyRep)
    }

    fun deleteChecked() {
        val iterator = _alarms.iterator()
        while (iterator.hasNext()) {
            val element = iterator.next()
            if (element.checked) iterator.remove()
        }
        alarmsUpdated()
    }

    fun changeAlarmChecked(item: Alarm, checked: Boolean) =
        _alarms.find { it.id == item.id }?.let { alarm -> alarm.checked = checked; }

    fun changeAlarmEnabled(item: Alarm, checked: Boolean) =
        _alarms.find { it.id == item.id }?.let { alarm -> alarm.enabled = checked; alarmsUpdated(); }

    private fun alarmsUpdated()
    {
        saveAlarms()
    }

    private fun saveAlarms(){
        Prefs.clear()
        Prefs.set(key="Num", value=alarms.size)
        _alarms.forEachIndexed { index, alarm ->
            Prefs.set(key="enabled_$index", value=alarm.enabled)
            Prefs.set(key="label_$index", value=alarm.label)
            Prefs.set(key="timeStart_$index", value=alarm.timeStart)
            Prefs.set(key="timeEnd_$index", value=alarm.timeEnd)
            Prefs.set(key="timeInterval_$index", value=alarm.timeInterval)
            Prefs.set(key="soundName_$index", value=alarm.soundName)
            Prefs.set(key="soundRep_$index", value=alarm.soundRep)
            for (i in 0..6)
                Prefs.set(key="weeklyRep_$index-$i", value=alarm.weeklyRep[i])
        }
    }

    private fun loadAlarms() {
        val numAlarms : Int = Prefs.get(key="Num")
        for (id in 0..numAlarms-1) {
            val alarm  = Alarm(id)
            alarm.checked = false
            alarm.enabled = Prefs.get(key="enabled_$id")
            alarm.label = Prefs.get(key="label_$id")
            alarm.timeStart = Prefs.get(key="timeStart_$id")
            alarm.timeEnd = Prefs.get(key="timeEnd_$id")
            alarm.timeInterval = Prefs.get(key="timeInterval_$id")
            if(alarm.timeInterval.toInt()>0) alarm.doTimeInterval = true
            alarm.soundName = Prefs.get(key="soundName_$id")
            alarm.soundRep = Prefs.get(key="soundRep_$id")
            for (i in 0..6)
                alarm.weeklyRep[i] = Prefs.get(key="weeklyRep_$id-$i")
            _alarms.add(alarm)
        }
    }
}

