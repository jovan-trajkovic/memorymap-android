package trajkovic.pora.memorymap

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.room.Room
import trajkovic.pora.memorymap.dao.LocationLogDatabase

class MyApplication: Application() {
    lateinit var database: LocationLogDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext,LocationLogDatabase::class.java, "location_logs_db").build()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reminder Notifications"
            val descriptionText = "Notifies when you haven't logged a new place in a while"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("reminder_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}