package com.routinealarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.routinealarm.helpers.SoundManager
import java.util.Calendar

const val RMNDRNOTITITLEKEY : String = "RoutineAlarm"
const val RMNDRNOTIID : Int = 0

object ScheduleNotification {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(
        context: Context,
        hour: Int,
        minute: Int,
        title: String
    ) {
        val intent = Intent(context.applicationContext, ReminderReceiver::class.java)
        intent.setAction("RoutineAlarm")

        intent.putExtra(RMNDRNOTITITLEKEY, title)
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            RMNDRNOTIID,
            intent,
            PendingIntent.FLAG_MUTABLE
        )


        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.set(year, month, day, hour, minute)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(), //calendar.timeInMillis,
            pendingIntent
        )

        Log.i("ALARM","scheduleNotification $hour $minute $year $month $day")
    }

}

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val scheduleNotificationService = context?.let { ReminderNotification(it) }
        val title: String? = intent?.getStringExtra(RMNDRNOTITITLEKEY)
        scheduleNotificationService?.playScheduledSound(title)
        Log.i("ALARM","scheduleNotificationService")
    }
}

class ReminderNotification(private val context: Context) {

    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun playScheduledSound(title: String?) {
        SoundManager.play("chime", 3)
        Log.i("ALARM","playScheduledSound")
    }
}