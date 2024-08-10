package com.routinealarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.routinealarm.MainActivity.Companion.appContext
import com.routinealarm.helpers.SoundManager
import java.util.Calendar

const val RMNDRNOTITITLEKEY : String = "RoutineAlarm"

object ScheduleNotification {

    @SuppressLint("ScheduleExactAlarm")
    fun schedule(
        hour: Int,
        minute: Int,
        requestCode : Int,
        title: String
    ) {
        val intent = Intent(appContext.applicationContext, ReminderReceiver::class.java)
        intent.setAction("RoutineAlarm")

        intent.putExtra(RMNDRNOTITITLEKEY, title)
        val pendingIntent = PendingIntent.getBroadcast(
            appContext.applicationContext,
            requestCode,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.set(year, month, day, hour, minute)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    fun clearAll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancelAll()
        }
    }

    fun clear(requestCode : Int) {
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            requestCode,
            Intent(appContext.applicationContext,ReminderReceiver::class.java),
            PendingIntent.FLAG_MUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val scheduleNotificationService = context?.let { ReminderNotification() }
        val title: String? = intent?.getStringExtra(RMNDRNOTITITLEKEY)
        scheduleNotificationService?.playScheduledSound(title)
    }
}

class ReminderNotification {

    fun playScheduledSound(title: String?) {
        val reps : Int = title?.substring(0, 2)?.toInt() ?: 0
        val sound : String = title?.substring(2).toString()
        SoundManager.play(sound, reps)
    }
}