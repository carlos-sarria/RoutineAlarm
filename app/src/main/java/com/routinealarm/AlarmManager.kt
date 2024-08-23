package com.routinealarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.routinealarm.MainActivity.Companion.appContext
import com.routinealarm.helpers.SoundManager
import java.util.Calendar
import java.util.Date
import com.routinealarm.ViewModel as appViewModel

const val RMNDRNOTITITLEKEY : String = "RoutineAlarm"

fun getExactTime(hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH),
        hour,
        minute,
        0)
    return calendar.timeInMillis
}

fun getCurrentTime () : Long {
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    return calendar.timeInMillis
}

fun getCurrentWeekDay () : Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.DAY_OF_WEEK) - 1 // From 0 to 6 (Mon to Sun)
}

fun getNextAlarm(requestCode : Int) : Long {
    val alarm : Alarm? = appViewModel().alarms.find { alarm -> alarm.requestCode == requestCode }
    if(alarm == null) return 0

    var alarmTime = getExactTime(hour(alarm.timeStart), minute(alarm.timeStart))
    val currentTime = getCurrentTime ()

    var currentRep  = 0
    val numIntervals = alarm.numIntervals.toInt()
    val intervalLength : Long = alarm.timeInterval.toLong() * 60000

    if(alarmTime < currentTime &&  (alarmTime +(numIntervals * intervalLength)) < currentTime)
    {
        alarmTime += (24*60*60*1000) // Add one day because alarm is set in the past
    }

    if (intervalLength > 0 && numIntervals > 0) {
        currentRep  = ((currentTime - alarmTime) / intervalLength).toInt()
    }

    var returnTime : Long = 0
    if (currentRep+1 == numIntervals) {
        val today : Int = getCurrentWeekDay()
        var dayCount = 0
        while(dayCount<7)
        {
            val nextDay : Int = (today + dayCount + 1) % 7
            if(alarm.weeklyRep[nextDay]){
                returnTime = alarmTime + nextDay * (24*60*60*1000)
                break
            }
            dayCount++
        }
    }
    else{
        returnTime = alarmTime + (currentRep+1) * intervalLength
    }
    return returnTime
}

object ScheduleNotification {

    var firstTimeAlarm: Boolean by  mutableStateOf(false)

    private fun getIntent (requestCode : Int) : PendingIntent {
        val intent = Intent(appContext.applicationContext, ReminderReceiver::class.java)
        intent.setAction("RoutineAlarm")

        intent.putExtra(RMNDRNOTITITLEKEY, requestCode)
        val pendingIntent = PendingIntent.getBroadcast(
            appContext.applicationContext,
            requestCode,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        return pendingIntent
    }

    @SuppressLint("ScheduleExactAlarm")
    fun schedule(
        hour: Int,
        minute: Int,
        requestCode : Int
    ) {
        val pendingIntent = getIntent(requestCode)
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        firstTimeAlarm = true

        val info : AlarmManager.AlarmClockInfo = AlarmManager.AlarmClockInfo(
            getExactTime(hour, minute) ,
            pendingIntent
        )
        alarmManager.setAlarmClock(info, pendingIntent)
    }

    fun schedule(
        milliseconds: Long,
        requestCode : Int
    ) {
        val pendingIntent = getIntent(requestCode)
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val info : AlarmManager.AlarmClockInfo = AlarmManager.AlarmClockInfo(
            milliseconds,
            pendingIntent
        )
        alarmManager.setAlarmClock(info, pendingIntent)
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, milliseconds, pendingIntent)
    }

    fun clearAll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancelAll()
        }
    }

    fun clear(requestCode : Int) {
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getIntent(requestCode)
        alarmManager.cancel(pendingIntent)
    }
}

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val scheduleNotificationService = context?.let { ReminderNotification() }
        val requestCode: Int = intent?.getIntExtra(RMNDRNOTITITLEKEY,0)!!
        scheduleNotificationService?.playScheduledSound(requestCode)
    }
}

class ReminderNotification {

    fun playScheduledSound(requestCode: Int) {
        val alarm : Alarm? = appViewModel().alarms.find { alarm -> alarm.requestCode == requestCode }
        if(alarm == null) return

        // Delete current alarm and add next alarm
        ScheduleNotification.clear(requestCode)
        val nextAlarmMilliseconds = getNextAlarm(requestCode)
        if(nextAlarmMilliseconds>0) ScheduleNotification.schedule(nextAlarmMilliseconds, requestCode)

        // Sound alarm
        val soundAlarm : Boolean = !ScheduleNotification.firstTimeAlarm
        if(soundAlarm) {
            SoundManager.play(alarm.soundName, alarm.soundRep.toInt())
        }
        else{
            ScheduleNotification.firstTimeAlarm = false
        }
    }
}