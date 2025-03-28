package com.routinealarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_DAY
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.routinealarm.GlobalData.Companion.appContext
import com.routinealarm.GlobalData.Companion.appViewModel
import com.routinealarm.helpers.SoundManager
import java.util.Calendar
import java.util.Date
import kotlin.math.abs

internal const val RMNDRNOTITITLEKEY = "RoutineAlarm"

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
    val alarm : Alarm? = appViewModel.alarms.find { alarm -> alarm.requestCode == requestCode }
    if(alarm == null) return 0

    var alarmTime = getExactTime(hour(alarm.timeStart), minute(alarm.timeStart))
    val currentTime = getCurrentTime ()

    var currentRep  = 0
    val numIntervals = alarm.numIntervals.toInt()
    val intervalLength : Long = alarm.timeInterval.toLong() * 60000

    if(alarmTime < currentTime &&  (alarmTime +(numIntervals * intervalLength)) < currentTime)
    {
        alarmTime += INTERVAL_DAY // Add one day because alarm is set in the past
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
                returnTime = alarmTime + nextDay * INTERVAL_DAY
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

    private val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

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

    fun schedule(
        hour: Int,
        minute: Int,
        requestCode : Int
    ) {
        schedule(getExactTime(hour, minute), requestCode)
    }

    fun schedule(
        milliseconds: Long,
        requestCode : Int
    ) {
        val pendingIntent = getIntent(requestCode)

        val info : AlarmManager.AlarmClockInfo = AlarmManager.AlarmClockInfo(
            milliseconds,
            pendingIntent
        )

        alarmManager.setAlarmClock(info, pendingIntent)
    }

    fun clearAll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

            alarmManager.cancelAll()
        }
    }

    fun clear(requestCode : Int) {
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
        val alarm : Alarm? = appViewModel.alarms.find { alarm -> alarm.requestCode == requestCode }
        if(alarm == null) return

        // Get the current alarm triggering time
        val prevScheduledTime : Long = alarm.scheduledTime

        // Delete current alarm and add next alarm
        ScheduleNotification.clear(requestCode)
        val nextAlarmMilliseconds = getNextAlarm(requestCode)
        if(nextAlarmMilliseconds>0)
            ScheduleNotification.schedule(nextAlarmMilliseconds, requestCode)
        alarm.scheduledTime = nextAlarmMilliseconds

        // Sound alarm
        // Android will trigger any alarm that was set in the past
        // Avoid sounding the alarm if it was set in less than 30 seconds
        val curr = getCurrentTime()
        Log.i("ALARM","$prevScheduledTime $curr")
        if ( abs(getCurrentTime()-prevScheduledTime) < 30000L )
         SoundManager.play(alarm.soundName, alarm.soundRep.toInt())
    }
}