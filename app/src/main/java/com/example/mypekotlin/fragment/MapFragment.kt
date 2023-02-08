package com.example.mypekotlin.fragment

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mypekotlin.R
import com.example.mypekotlin.databinding.FragmentMapBinding
import com.example.mypekotlin.model.Marker
import com.example.mypekotlin.viewModel.MapViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "binding is null"
        }
    private val viewModel: MapViewModel by viewModels()

    private var map: GoogleMap? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            findNavController().navigate(
                MapFragmentDirections.actionMapFragmentToPermissionFragment()
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        createLocationCallback()

        return binding.root
    }

    override fun onResume() {
        Log.d("MyTag", "onResume")
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(p0: GoogleMap) {
        Log.d("MyTag", "onMapReady")
        map = p0

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.markers.collect { markers ->
                for (marker in markers) {
                    addMarker(marker)
                }
            }
        }

    }

    private fun addMarker(marker: Marker): com.google.android.gms.maps.model.Marker? {
        return map?.addMarker(
            MarkerOptions()
                .position(LatLng(marker.latitude, marker.longitude))
                .title(marker.name)
        )
    }

    private fun animateCameraToLocation(location: Location){
        map?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    /*markers[1].latitude,*/
                    location.latitude,
                    location.longitude
                ), 12f
            )
        )
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(10000).build()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun createLocationCallback() {
        var marker: com.google.android.gms.maps.model.Marker? = null

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations){
                    marker?.remove()
                    //to be called it only once
                    if(marker == null) animateCameraToLocation(location)
                    marker = addMarker(Marker("User", location.latitude, location.longitude))
                }
            }
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}