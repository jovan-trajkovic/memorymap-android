package trajkovic.pora.memorymap

import android.app.Application
import androidx.room.Room
import trajkovic.pora.memorymap.dao.LocationLogDatabase

class MyApplication: Application() {
    lateinit var database: LocationLogDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext,LocationLogDatabase::class.java, "location_logs_db").build()
    }
}