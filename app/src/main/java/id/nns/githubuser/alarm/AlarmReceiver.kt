package id.nns.githubuser.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import id.nns.githubuser.R
import id.nns.githubuser.activity.MainActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_REPEATING = "Repeating Alarm"
        const val EXTRA_MESSAGE = "message"
        private const val ID_REPEATING = 100
        private const val TIME_FORMAT = "09:00"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        if (message != null) {
            showAlarmNotification(context, message)
        }
    }

    fun setRepeatingAlarm(context: Context, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)

        val timeArray = TIME_FORMAT.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        Toast.makeText(context, "Repeating alarm is ON!", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, "Repeating alarm is OFF", Toast.LENGTH_SHORT).show()
    }

    private fun showAlarmNotification(context: Context, message: String) {
        val channelId = "Channel_1"
        val channelName = "AlarmManager channel"

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationManagerCompat =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_access_time_black)
                .setContentTitle(TYPE_REPEATING)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManagerCompat.notify(ID_REPEATING, notification)
    }

}