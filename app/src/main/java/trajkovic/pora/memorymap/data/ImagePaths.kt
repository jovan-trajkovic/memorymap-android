package trajkovic.pora.memorymap.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "image_paths", foreignKeys = [
        ForeignKey(
            entity = LocationLog::class,
            parentColumns = ["id"],
            childColumns = ["locationLogId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["locationLogId"])]
)
data class ImagePaths(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val imagePath: String,
    val locationLogId: String
)
