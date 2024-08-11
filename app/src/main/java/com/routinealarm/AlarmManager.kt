package com.routinealarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.routinealarm.MainActivity.Companion.appContext
import com.routinealarm.helpers.SoundManager
import java.util.Calendar
import java.util.Date
import com.routinealarm.ViewModel as appViewModel

const val RMNDRNOTITITLEKEY : String = "RoutineAlarm"

object ScheduleNotification {

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

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        //calendar.time = Date()
        calendar.set(year, month, day, hour, minute)

        val info : AlarmManager.AlarmClockInfo = AlarmManager.AlarmClockInfo(
            calendar.timeInMillis - (30*1000),
            pendingIntent
        )
        alarmManager.setAlarmClock(info, pendingIntent)
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
        val requestCode: Int? = intent?.getIntExtra(RMNDRNOTITITLEKEY,0)
        scheduleNotificationService?.playScheduledSound(requestCode)
    }
}

class ReminderNotification {

    fun playScheduledSound(requestCode: Int?) {
        val viewModel = appViewModel()
        val alarm : Alarm? = viewModel.alarms.find { alarm -> alarm.requestCode == requestCode }
        if(alarm == null) return

        SoundManager.play(alarm.soundName, alarm.soundRep.toInt())
    }
}