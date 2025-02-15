package trajkovic.pora.memorymap.fragments

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import trajkovic.pora.memorymap.LocationLogViewModel
import trajkovic.pora.memorymap.LocationLogViewModelFactory
import trajkovic.pora.memorymap.MyApplication
import trajkovic.pora.memorymap.R
import trajkovic.pora.memorymap.adapters.AddLogImagePreviewAdapter
import trajkovic.pora.memorymap.data.LocationLog
import trajkovic.pora.memorymap.databinding.FragmentAddLogBinding
import java.io.File
import java.io.FileOutputStream

class AddLogFragment : Fragment() {

    private var _binding: FragmentAddLogBinding? = null
    private val binding get() = _binding!!

    private lateinit var selectedImagesAdapter: AddLogImagePreviewAdapter
    private val selectedImageUris = mutableListOf<Uri>()

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

        var rating = 0
        binding.ratingSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rating = progress + 1
                val ratingText = "Rating: $rating"
                binding.ratingLabel.text = ratingText
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        var latitude: Double? = null
        var longitude: Double? = null
        val mapFragment = childFragmentManager.findFragmentById(R.id.addLogMapView) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            googleMap.setOnMapClickListener { latLng ->
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(latLng))
                latitude = latLng.latitude
                longitude = latLng.longitude
            }

            googleMap.setOnCameraMoveStartedListener {//enables map scrolling on the horizontal orientation
                mapFragment.view?.parent?.requestDisallowInterceptTouchEvent(true)
            }

            googleMap.setOnCameraIdleListener {
                mapFragment.view?.parent?.requestDisallowInterceptTouchEvent(false)
            }
        }

        binding.addLogBtn.setOnClickListener {
            lifecycleScope.launch {
                val title = binding.titleField.text.toString()
                val description = binding.descriptionField.text.toString()
                val thumbnailPath = copyImagesToAppFiles().firstOrNull()

                if (title.isNotBlank() && description.isNotBlank() && latitude != null && longitude != null) {
                    val newLog = LocationLog(
                        name = title,
                        description = description,
                        rating = rating.toFloat(),
                        latitude = latitude!!,
                        longitude = longitude!!,
                        thumbnailPath = thumbnailPath?.toString()
                    )
                    viewModel.addLog(newLog)

                    //todo: add images to the database - create function in the dao

                    Toast.makeText(requireContext(), "Log added!", Toast.LENGTH_SHORT).show()

                    binding.titleField.text.clear()
                    binding.descriptionField.text.clear()
                    binding.ratingSeekBar.progress = 4
                    mapFragment?.getMapAsync { googleMap ->
                        googleMap.clear()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Data was added incorrectly",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        selectedImagesAdapter = AddLogImagePreviewAdapter(selectedImageUris)
        binding.thumbnailsRecyclerView.adapter = selectedImagesAdapter
        binding.thumbnailsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.addPhotosBtn.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                pickImagesLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            else
                Toast.makeText(requireContext(), "This feature is only supported in Android 11 and above!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val pickImagesLauncher = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(6)) { uris ->
        if (uris.isNotEmpty()) {
            selectedImageUris.clear()
            selectedImageUris.addAll(uris)
            selectedImagesAdapter.notifyDataSetChanged()
            binding.thumbnailsRecyclerView.visibility = View.VISIBLE
        } else {
            Toast.makeText(requireContext(), "No images selected", Toast.LENGTH_SHORT).show()
            binding.thumbnailsRecyclerView.visibility = View.GONE
        }
    }

    private suspend fun copyImagesToAppFiles(): List<Uri> = withContext(Dispatchers.IO) {
        val savedUris = mutableListOf<Uri>()
        val appContext = requireContext().applicationContext

        selectedImageUris.forEach { uri ->
            try {
                val inputStream = appContext.contentResolver.openInputStream(uri)
                val fileName = "log_image_${System.currentTimeMillis()}.jpg"
                val file = File(appContext.filesDir, fileName)

                inputStream?.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }

                // Get the URI of the saved file
                val savedUri = Uri.fromFile(file)
                savedUris.add(savedUri)
            } catch (e: Exception) {
                Log.e("AddLogFragment", "Error saving image: ${e.message}")
                Toast.makeText(requireContext(), "Failed to save an image", Toast.LENGTH_SHORT).show()
            }
        }

        savedUris
    }
}