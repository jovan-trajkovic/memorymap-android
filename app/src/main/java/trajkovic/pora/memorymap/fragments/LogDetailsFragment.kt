package trajkovic.pora.memorymap.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import trajkovic.pora.memorymap.R
import trajkovic.pora.memorymap.data.LocationLog
import trajkovic.pora.memorymap.databinding.FragmentAddLogBinding
import trajkovic.pora.memorymap.databinding.FragmentLogDetailsBinding
import java.util.Calendar

class LogDetailsFragment : Fragment() {
    private var _binding: FragmentLogDetailsBinding? = null
    private val binding get() = _binding!!

    private var _log: LocationLog? = null
    private val log get() = _log!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _log = arguments?.getParcelable("log")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = log.dateAdded
        binding.titleLabel.text = log.name
        binding.descriptionLabel.text = log.description
        binding.ratingLabel.text = "Rating: ${log.rating}"
        binding.latitudeLabel.text = "Latitude: ${log.latitude}"
        binding.longitudeLabel.text = "Longitude: ${log.longitude}"
        binding.dateAddedLabel.text = "Date added: ${calendar.get(Calendar.DATE)}.${calendar.get(Calendar.MONTH)}.${calendar.get(Calendar.YEAR)}."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}