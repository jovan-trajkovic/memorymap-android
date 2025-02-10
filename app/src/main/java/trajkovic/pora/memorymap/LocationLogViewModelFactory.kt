package trajkovic.pora.memorymap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import trajkovic.pora.memorymap.dao.LocationLogDao

class LocationLogViewModelFactory(private val dao: LocationLogDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationLogViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}