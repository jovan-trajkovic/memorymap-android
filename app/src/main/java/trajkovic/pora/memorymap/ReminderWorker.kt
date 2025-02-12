package trajkovic.pora.memorymap

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.util.Calendar

class ReminderWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val context = applicationContext

        val app = context.applicationContext as MyApplication
        val dao = app.database.dao

        val latestLog = dao.getLastLog()
        val currentDate = Calendar.getInstance().timeInMillis
        val daysSinceLastLog = (currentDate - latestLog.dateAdded) / (1000 * 60 * 60 * 24)

        Log.d("ReminderWorker", "Days since last log: $daysSinceLastLog")

        if (daysSinceLastLog >= 1) {
            sendReminderNotification(context)
        }

        return Result.success()
    }

    private fun sendReminderNotification(context: Context) {

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("app://memorymap/addlog")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(R.drawable.baseline_add_location_24)
            .setContentTitle("Visit a New Place!")
            .setContentText("You haven't logged a new place in over a week. Time to explore!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }
}
