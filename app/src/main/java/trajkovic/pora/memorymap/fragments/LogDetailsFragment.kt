package trajkovic.pora.memorymap.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import trajkovic.pora.memorymap.LocationLogViewModel
import trajkovic.pora.memorymap.LocationLogViewModelFactory
import trajkovic.pora.memorymap.MyApplication
import trajkovic.pora.memorymap.databinding.FragmentLogDetailsBinding
import java.util.Calendar

class LogDetailsFragment : Fragment() {
    private var _binding: FragmentLogDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: LocationLogViewModel by activityViewModels {
            LocationLogViewModelFactory((requireActivity().application as MyApplication).database.dao)
        }
        val index = arguments?.getInt("log_index") ?: -1
        val log = viewModel.logs.value.getOrNull(index)

        log?.let {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = log.dateAdded
            val ratingText = "Rating: ${log.rating}"
            val latitudeText = "Latitude: ${log.latitude}"
            val longitudeText = "Longitude: ${log.longitude}"
            val dateAddedText =
                "Date added: ${calendar.get(Calendar.DATE)}.${calendar.get(Calendar.MONTH) + 1}.${
                    calendar.get(Calendar.YEAR)
                }."
            binding.titleLabel.text = log.name
            binding.descriptionLabel.text = log.description
            binding.ratingLabel.text = ratingText
            binding.latitudeLabel.text = latitudeText
            binding.longitudeLabel.text = longitudeText
            binding.dateAddedLabel.text = dateAddedText
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}