package trajkovic.pora.memorymap.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import trajkovic.pora.memorymap.adapters.LocationLogAdapter
import trajkovic.pora.memorymap.data.LocationLog
import trajkovic.pora.memorymap.databinding.FragmentListBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        //return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LocationLogAdapter(logs) { log->
            //TODO: Handle List Item click
            Toast.makeText(requireContext(),"ITEM CLICKED: ${log.name}", Toast.LENGTH_SHORT).show()
        }
        binding.logList.layoutManager = LinearLayoutManager(requireContext())
        binding.logList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
            }
    }*/
}