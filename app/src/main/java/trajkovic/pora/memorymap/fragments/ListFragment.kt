package trajkovic.pora.memorymap.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import trajkovic.pora.memorymap.MyApplication
import trajkovic.pora.memorymap.adapters.LocationLogAdapter
import trajkovic.pora.memorymap.data.LocationLog
import trajkovic.pora.memorymap.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val logs = listOf( // TODO: Replace with data from database
        LocationLog(name = "Eiffel Tower", description = "Visited in Paris", rating = 5f, latitude = 48.8584, longitude = 2.2945, thumbnailPath = null),
        LocationLog(name = "Grand Canyon", description = "Amazing views", rating = 4.5f, latitude = 36.1069, longitude = -112.1129, thumbnailPath = null)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as MyApplication
        val dao = app.database.dao
        val adapter = LocationLogAdapter(logs) { log->
            //TODO: Handle List Item click
            Toast.makeText(requireContext(),"ITEM CLICKED: ${log.name}", Toast.LENGTH_SHORT).show()
        }
        binding.logList.layoutManager = LinearLayoutManager(requireContext())
        binding.logList.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {
            dao.insertLog(logs[0])
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}