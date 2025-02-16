package trajkovic.pora.memorymap.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import trajkovic.pora.memorymap.LocationLogViewModel
import trajkovic.pora.memorymap.LocationLogViewModelFactory
import trajkovic.pora.memorymap.MyApplication
import trajkovic.pora.memorymap.adapters.LogDetailsImageAdapter
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
        val args: LogDetailsFragmentArgs by navArgs()
        val index = args.logIndex
        val log = viewModel.logs.value.getOrNull(index)

        log?.let {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = log.dateAdded
            val ratingText = "Rating: ${log.rating}"
            val dateAddedText =
                "Date added: ${calendar.get(Calendar.DATE)}.${calendar.get(Calendar.MONTH) + 1}.${
                    calendar.get(Calendar.YEAR)
                }."
            binding.titleLabel.text = log.name
            binding.descriptionLabel.text = log.description
            binding.ratingLabel.text = ratingText
            binding.dateAddedLabel.text = dateAddedText

            val logPicturesAdapter = LogDetailsImageAdapter(emptyList())
            binding.logImagesRecyclerView.adapter = logPicturesAdapter
            binding.logImagesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            if(log.thumbnailPath != null) {
                //todo: fix scrolling
                viewModel.getImages(log.id).observe(viewLifecycleOwner) { imageList ->
                    Log.d("DEBUG", imageList.toString())
                    logPicturesAdapter.submitList(imageList)
                    binding.logImagesRecyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}