package trajkovic.pora.memorymap.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import trajkovic.pora.memorymap.LocationLogViewModel
import trajkovic.pora.memorymap.LocationLogViewModelFactory
import trajkovic.pora.memorymap.MyApplication
import trajkovic.pora.memorymap.R
import trajkovic.pora.memorymap.adapters.LocationLogAdapter
import trajkovic.pora.memorymap.data.LocationLog
import trajkovic.pora.memorymap.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val placeholderLogs = listOf(
        LocationLog(id = "75023080-917a-47f9-b3c3-85ec214185b2",name = "Eiffel Tower", description = "Visited in Paris", rating = 5f, latitude = 48.8584, longitude = 2.2945, thumbnailPath = null),
        LocationLog(id = "75023080-917a-47f9-b3c3-85ec214185b5", name = "Grand Canyon", description = "Amazing views", rating = 4.5f, latitude = 36.1069, longitude = -112.1129, thumbnailPath = null)
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

        val viewModel: LocationLogViewModel by activityViewModels {
            LocationLogViewModelFactory((requireActivity().application as MyApplication).database.dao)
        }

        val adapter = LocationLogAdapter(emptyList(),
            onItemClick = { index ->
                val bundle = Bundle().apply {
                    putInt("log_index", index)
                }
                val fragment = LogDetailsFragment().apply {
                    arguments = bundle
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            onDelete = { log ->
                viewModel.deleteLog(log)
            })

        binding.logList.layoutManager = LinearLayoutManager(requireContext())
        binding.logList.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logs.collect { logs ->
                    adapter.updateLogs(logs)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}