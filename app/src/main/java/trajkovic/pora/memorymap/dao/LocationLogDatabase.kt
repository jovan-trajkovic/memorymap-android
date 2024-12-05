package trajkovic.pora.memorymap.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import trajkovic.pora.memorymap.data.ImagePaths
import trajkovic.pora.memorymap.data.LocationLog

@Database(
    entities = [LocationLog::class, ImagePaths::class], version = 1
)
abstract class LocationLogDatabase: RoomDatabase() {

    abstract val dao: LocationLogDao
}