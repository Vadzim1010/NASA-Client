package com.example.nasa.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nasa.data.util.log
import com.example.nasa.databinding.FragmentMapBinding
import com.example.nasa.domain.model.Resource
import com.example.nasa.utils.applyWindowInsets
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null

    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<MapViewModel>()

    private var googleMap: GoogleMap? = null

    private var locationListener: LocationSource.OnLocationChangedListener? = null

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionGranted ->
        if (permissionGranted) {
            initGoogleMap()
            viewModel.loadCurrentLocation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMapBinding.inflate(layoutInflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)

        subscribeOnCurrentLocation()
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
        _binding = null
        googleMap = null
    }

    @SuppressLint("MissingPermission")
    private fun initGoogleMap() = with(binding) {
        mapView.getMapAsync { map ->

            googleMap = map.apply {
                uiSettings.isCompassEnabled = true
                uiSettings.isZoomControlsEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
            map.isMyLocationEnabled = hasLocationPermission()

            mapView.applyWindowInsets(map)

            map.setLocationSource(object : LocationSource {
                override fun activate(listener: LocationSource.OnLocationChangedListener) {
                    locationListener = listener
                }

                override fun deactivate() {
                    locationListener = null
                }
            })

            subscribeLocationUpdates()

            viewModel.countriesFLow
                .onEach { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let { countries ->
                                countries.forEach {
                                    map.addMarker(LatLng(it.lat, it.lng), it.name)
                                }
                            }
                        }
                        is Resource.Error -> {
                            showToast(resource.throwable?.message ?: "")
                        }
                        is Resource.Loading -> {
                            //no op
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            map.setOnMarkerClickListener { marker ->
                findNavController().navigate(
                    MapFragmentDirections.toBottomSheet(
                        marker.title ?: ""
                    )
                )
                true
            }
        }
    }

    private fun subscribeOnCurrentLocation() {
        viewModel.currentLocationFlow
            .onEach { location ->
                log(location.toString())
                location?.let { moveCameraToLocation(it) }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun subscribeLocationUpdates() {
        viewModel
            .locationFlow
            .onEach { location ->
                locationListener?.onLocationChanged(location)
                delay(1000)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun moveCameraToLocation(location: Location) {
        val current = LatLng(location.latitude, location.longitude)
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(current, 17f)
        )
    }

    private fun GoogleMap.addMarker(latLng: LatLng, tittle: String) {
        this.addMarker(
            MarkerOptions()
                .title(tittle)
                .position(latLng)
        )
    }

    private fun showToast(massage: String) {
        Toast.makeText(requireContext(), massage, Toast.LENGTH_SHORT).show()
    }

    private fun requestPermission(permission: String) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission,
            ) == PackageManager.PERMISSION_GRANTED -> {
                initGoogleMap()
                viewModel.loadCurrentLocation()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                AlertDialog.Builder(requireContext())
                    .setTitle("Need permission")
                    .setMessage("Please provide permission")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        permissionLauncher.launch(permission)
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            }
            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }
}
