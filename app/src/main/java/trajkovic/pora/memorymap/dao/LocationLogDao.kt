package trajkovic.pora.memorymap.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import trajkovic.pora.memorymap.data.ImagePaths
import trajkovic.pora.memorymap.data.LocationLog

@Dao
interface LocationLogDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLog(log: LocationLog)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLogs(logs: List<LocationLog>)

    @Delete
    suspend fun deleteLog(log: LocationLog)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhotos(photos: List<ImagePaths>)

    @Query("SELECT * FROM location_logs ORDER BY dateAdded DESC")
    fun getAllLogs(): Flow<List<LocationLog>>

    @Query("SELECT * FROM location_logs ORDER BY dateAdded DESC LIMIT 1")
    fun getLastLog(): LocationLog

    @Query("SELECT * FROM image_paths WHERE locationLogId = :id")
    fun getImages(id: String): Flow<List<ImagePaths>>
}