package trajkovic.pora.memorymap

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import trajkovic.pora.memorymap.dao.LocationLogDao
import trajkovic.pora.memorymap.data.ImagePaths
import trajkovic.pora.memorymap.data.LocationLog

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
            dao.deleteLog(log)
        }
    }

    fun addImages(uris: List<String>, logId: String) {
        viewModelScope.launch {
            val paths = mutableListOf<ImagePaths>()
            uris.forEach{ uri ->
                paths.add(ImagePaths(imagePath = uri, locationLogId = logId))
            }
            dao.insertPhotos(paths)
        }
    }

    fun getImages(logId: String): LiveData<List<ImagePaths>> {
        return dao.getImages(logId).asLiveData()
    }
}