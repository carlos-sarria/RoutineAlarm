package com.routinealarm

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.core.app.NotificationCompat
import java.util.Calendar

val RMNDR_NOTI_TITLE_KEY : String = "RoutineAlarm"
val RMNDR_NOTI_ID : Int = 0

//class ScheduleNotification {
//
//    @OptIn(ExperimentalMaterial3Api::class)
//    fun scheduleNotification(
//        context: Context,
//        timePickerState: TimePickerState,
//        datePickerState: DatePickerState,
//        title: String
//    ) {
//        val intent = Intent(context.applicationContext, ReminderReceiver::class.java)
//
//        intent.putExtra("RMNDR_NOTI_TITLE_KEY", title)
//        val pendingIntent = PendingIntent.getBroadcast(
//            context.applicationContext,
//            0,
//            intent,
//            PendingIntent.FLAG_MUTABLE
//        )
//
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val selectedDate = Calendar.getInstance().apply {
//            timeInMillis = datePickerState.selectedDateMillis!!
//        }
//
//        val year = selectedDate.get(Calendar.YEAR)
//        val month = selectedDate.get(Calendar.MONTH)
//        val day = selectedDate.get(Calendar.DAY_OF_MONTH)
//
//        val calendar = Calendar.getInstance()
//        calendar.set(year, month, day, timePickerState.hour, timePickerState.minute)
//
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            pendingIntent
//        )
//
//        Toast.makeText(context, "Reminder set!!", Toast.LENGTH_SHORT).show()
//    }
//
//}
//
//class ReminderReceiver {
//    override fun onReceive(context: Context?, intent: Intent?) {
//        val scheduleNotificationService = context?.let { ReminderNotification(it) }
//        val title: String? = intent?.getStringExtra(RMNDR_NOTI_TITLE_KEY)
//        scheduleNotificationService?.sendReminderNotification(title)
//    }
//}
//
//class ReminderNotification(private val context: Context) {
//
//    private val notificationManager = context.getSystemService(NotificationManager::class.java)
//
//    fun sendReminderNotification(title: String?) {
//      //  notificationManager.notify(RMNDR_NOTI_ID, notification)
//    }
//}