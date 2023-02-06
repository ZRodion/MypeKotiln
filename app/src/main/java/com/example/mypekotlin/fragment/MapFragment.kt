package com.example.mypekotlin.fragment

import android.content.pm.PackageManager
import android.os.Bundle
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
import com.example.mypekotlin.viewModel.MapViewModel
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

        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.markers.collect { markers ->
                for (marker in markers) {
                    map.addMarker(
                        MarkerOptions()
                            .position(LatLng(marker.latitude, marker.longitude))
                            .title(marker.name)
                    )
                }
                if (markers.isNotEmpty())
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                markers[1].latitude,
                                markers[0].longitude
                            ), 8f
                        )
                    )
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}