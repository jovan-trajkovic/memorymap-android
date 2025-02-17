package trajkovic.pora.memorymap

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import trajkovic.pora.memorymap.dao.LocationLogDao
import trajkovic.pora.memorymap.data.ImagePaths
import trajkovic.pora.memorymap.data.LocationLog
import java.io.File

class LocationLogViewModel(private val dao: LocationLogDao) : ViewModel() {

    val logs: StateFlow<List<LocationLog>> = dao.getAllLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addLog(log: LocationLog) {
        viewModelScope.launch {
            dao.insertLog(log)
        }
    }

    fun deleteLog(log: LocationLog) {
        viewModelScope.launch {
            val images = dao.getImages(log.id).first()

            images.forEach { imagePath ->
                val uri = Uri.parse(imagePath.imagePath)
                val file = File(uri.path!!)
                if (file.exists()) {
                    file.delete()
                }
            }

            dao.deleteLog(log)
        }
    }

    fun addImagesForLog(imageUris: List<Uri>, logId: String) {
        viewModelScope.launch {
            val imagePaths = imageUris.map { uri ->
                ImagePaths(
                    locationLogId = logId,
                    imagePath = uri.toString()
                )
            }
            dao.insertPhotos(imagePaths)
        }
    }

    fun getImages(logId: String): LiveData<List<ImagePaths>> {
        return dao.getImages(logId).asLiveData()
    }
}