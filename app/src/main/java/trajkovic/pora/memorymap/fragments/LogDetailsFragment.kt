package trajkovic.pora.memorymap.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import trajkovic.pora.memorymap.data.LocationLog
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
        val ratingText = "Rating: ${log.rating}"
        val latitudeText = "Latitude: ${log.latitude}"
        val longitudeText = "Longitude: ${log.longitude}"
        val dateAddedText = "Date added: ${calendar.get(Calendar.DATE)}.${calendar.get(Calendar.MONTH) + 1}.${calendar.get(Calendar.YEAR)}."
        binding.titleLabel.text = log.name
        binding.descriptionLabel.text = log.description
        binding.ratingLabel.text = ratingText
        binding.latitudeLabel.text = latitudeText
        binding.longitudeLabel.text = longitudeText
        binding.dateAddedLabel.text = dateAddedText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}