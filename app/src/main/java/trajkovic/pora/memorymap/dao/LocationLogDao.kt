package trajkovic.pora.memorymap.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import trajkovic.pora.memorymap.data.ImagePaths
import trajkovic.pora.memorymap.data.LocationLog

@Dao
interface LocationLogDao {

    @Insert
    fun insertLog(log: LocationLog)

    @Delete
    fun deleteLog(log: LocationLog)

    @Query("SELECT * FROM location_logs ORDER BY dateAdded DESC")
    fun getAllLogs(): LiveData<List<LocationLog>>

    @Query("SELECT * FROM image_paths WHERE locationLogId = :id")
    fun getImages(id: String): LiveData<List<ImagePaths>>
}