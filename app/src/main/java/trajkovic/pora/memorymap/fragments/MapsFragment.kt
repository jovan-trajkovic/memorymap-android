package trajkovic.pora.memorymap.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import trajkovic.pora.memorymap.LocationLogViewModel
import trajkovic.pora.memorymap.LocationLogViewModelFactory
import trajkovic.pora.memorymap.MyApplication
import trajkovic.pora.memorymap.R
import trajkovic.pora.memorymap.data.LocationLog

class MapsFragment : Fragment() {

    private lateinit var button: Button
    private var cameraPosition: CameraPosition? = null

    private var tempGoogleMap: GoogleMap? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {

            tempGoogleMap?.let { enableLocationOnMap(it) }
            tempGoogleMap = null
        } else {
            tempGoogleMap = null
            Toast.makeText(context, "Location permissions denied.", Toast.LENGTH_SHORT).show()
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            enableLocationOnMap(googleMap)
        } else {
            Toast.makeText(
                context,
                "Location permissions are required to show your location.",
                Toast.LENGTH_SHORT
            ).show()

            tempGoogleMap = googleMap
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

        }

        val viewModel: LocationLogViewModel by activityViewModels {
            LocationLogViewModelFactory((requireActivity().application as MyApplication).database.dao)
        }
        var queriedLogs: List<LocationLog>

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logs.collect { logs ->
                    queriedLogs = logs

                    googleMap.clear()

                    for ((index,log) in queriedLogs.withIndex()) {
                        val logMarker = MarkerOptions()
                            .position(LatLng(log.latitude, log.longitude))
                            .title(log.name)

                        googleMap.addMarker(logMarker)?.tag = index
                    }
                }
            }
        }

        googleMap.setOnMarkerClickListener { marker ->
            marker.showInfoWindow()

            button.visibility = View.VISIBLE
            button.setOnClickListener {
                cameraPosition = googleMap.cameraPosition

                val logIndex = marker.tag as? Int
                if(logIndex != null) {
                    val action =
                        MapsFragmentDirections.actionMapsFragmentToLogDetailsFragment(logIndex)
                    findNavController().navigate(action)
                }
            }
            true
        }

        googleMap.setOnMapClickListener {
            button.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById(R.id.detailsBtn)
        savedInstanceState?.getParcelable<CameraPosition>("camera_position")?.let {
            cameraPosition = it
            Toast.makeText(context,"Set previous cameraPosition", Toast.LENGTH_SHORT).show()
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        cameraPosition?.let {
            outState.putParcelable("camera_position", it)
        }
    }

    private fun enableLocationOnMap(googleMap: GoogleMap) {
        try {
            googleMap.isMyLocationEnabled = true
            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireContext())

            if (cameraPosition != null) {
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition!!))
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
                    if (lastLocation != null) {
                        val userLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(userLocation, 12f)
                        )
                    } else {
                        Toast.makeText(context, "Unable to retrieve location.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            Toast.makeText(context, "Unable to enable My Location. Permission denied.", Toast.LENGTH_SHORT).show()
        }
    }
}