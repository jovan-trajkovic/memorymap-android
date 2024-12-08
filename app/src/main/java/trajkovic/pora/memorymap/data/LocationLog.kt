package trajkovic.pora.memorymap.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Calendar
import java.util.UUID

@Parcelize
@Entity(tableName = "location_logs")
data class LocationLog(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val rating: Float,
    val latitude: Double,
    val longitude: Double,
    val thumbnailPath: String?,
    val dateAdded:Long = Calendar.getInstance().timeInMillis
) : Parcelable