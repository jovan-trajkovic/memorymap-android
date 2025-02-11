package trajkovic.pora.memorymap.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import trajkovic.pora.memorymap.LocationLogViewModel
import trajkovic.pora.memorymap.LocationLogViewModelFactory
import trajkovic.pora.memorymap.MyApplication
import trajkovic.pora.memorymap.data.LocationLog
import trajkovic.pora.memorymap.databinding.FragmentAddLogBinding

class AddLogFragment : Fragment() {

    private var _binding: FragmentAddLogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: LocationLogViewModel by activityViewModels {
            LocationLogViewModelFactory((requireActivity().application as MyApplication).database.dao)
        }

        var rating = 0;
        binding.ratingSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rating = progress + 1
                val ratingText = "Rating: $rating"
                binding.ratingLabel.text = ratingText
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.addLogBtn.setOnClickListener {
            val title = binding.titleField.text.toString()
            val description = binding.descriptionField.text.toString()
            val latitude = binding.latitudeField.text.toString().toDoubleOrNull() ?: 0.0
            val longitude = binding.longitudeField.text.toString().toDoubleOrNull() ?: 0.0

            if (title.isNotBlank() && description.isNotBlank() && (-90 <= latitude && latitude <= 90) && (-180 <= longitude && longitude <= 180)) {
                val newLog = LocationLog(
                    name = title,
                    description = description,
                    rating = rating.toFloat(),
                    latitude = latitude,
                    longitude = longitude,
                    thumbnailPath = null
                )
                viewModel.addLog(newLog)

                Toast.makeText(requireContext(), "Log added!", Toast.LENGTH_SHORT).show()

                binding.titleField.text.clear()
                binding.descriptionField.text.clear()
                binding.latitudeField.text.clear()
                binding.longitudeField.text.clear()
                binding.ratingSeekBar.progress = 4
            } else {
                Toast.makeText(requireContext(), "Data was added incorrectly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}