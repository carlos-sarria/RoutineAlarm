package com.routinealarm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.routinealarm.helpers.Prefs
import kotlin.Int.Companion.MAX_VALUE
import kotlin.random.Random

data class Alarm (var id: Int)
{
    var checked by mutableStateOf(false)
    var enabled by mutableStateOf(true)
    var label by mutableStateOf("Alarm_%03d".format(id))
    var timeStart by mutableStateOf("08:00")
    var numIntervals by mutableStateOf("0")
    var timeInterval by mutableStateOf("0")
    var soundName by mutableStateOf("chime")
    var soundRep by mutableStateOf("1")
    var requestCode by mutableIntStateOf(Random.nextInt(0, MAX_VALUE))
    var weeklyRep by mutableStateOf(arrayOf(false, false, false, false, false, false,false))

}

var soundList = arrayOf("chime", "bell", "blip", "bong", "drum")
fun hour(string : String) : Int = string.substring(0, 2).toInt()
fun minute(string : String) : Int = string.substring(3, 5).toInt()

class ViewModel : ViewModel() {

    private var _alarms : MutableList<Alarm> = listOf<Alarm>().toMutableList() // Empty List

    init {
        loadAlarms()
    }

    val alarms: List<Alarm> // This is to been accessed from outside
        get() = _alarms // Implement get() for val

    fun add(alarm : Alarm) {
        _alarms.add(alarm)
    }

    fun copy(source : Alarm, destination : Alarm) {
        destination.checked = source.checked
        destination.enabled  = source.enabled
        destination.label = source.label
        destination.timeStart = source.timeStart
        destination.numIntervals = source.numIntervals
        destination.timeInterval = source.timeInterval
        destination.soundName = source.soundName
        destination.soundRep = source.soundRep
        destination.requestCode = source.requestCode
        destination.weeklyRep.copyInto(source.weeklyRep)
    }

    fun deleteChecked() {
        val iterator = _alarms.iterator()
        while (iterator.hasNext()) {
            val element = iterator.next()
            if (element.checked) {
                iterator.remove()
                ScheduleNotification.clear(element.requestCode)
            }
        }
        saveAlarms()
    }

    fun changeAlarmChecked(alarm: Alarm, checked: Boolean) =
        _alarms.find { it.id == alarm.id }?.let { alarm -> alarm.checked = checked; }

    fun changeAlarmEnabled(alarm: Alarm, checked: Boolean) =
        _alarms.find { it.id == alarm.id }?.let { alarm ->
            alarm.enabled = checked;
            if(alarm.enabled) setSystemAlarm(alarm) else removeSystemAlarm(alarm)
            saveAlarms();
        }

    fun removeSystemAlarm(alarm : Alarm)
    {
        var reps = alarm.numIntervals.toInt()
        while (reps >= 0) {
            ScheduleNotification.clear(alarm.requestCode+reps)
            reps--
        }
    }

    fun setSystemAlarm(alarm : Alarm)
    {
        var reps = alarm.numIntervals.toInt()
        var hour = hour(alarm.timeStart)
        var minute = minute(alarm.timeStart)
        while (reps >= 0) {
            ScheduleNotification.schedule(
                hour = hour,
                minute = minute,
                requestCode = alarm.requestCode+reps,
                title = alarm.soundRep.padStart(2, '0') + alarm.soundName
            )
            minute += alarm.timeInterval.toInt()
            if(minute>59) { minute %= 59; hour++}
            reps--
        }
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

