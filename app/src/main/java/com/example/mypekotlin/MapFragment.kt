package com.example.mypekotlin

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypekotlin.databinding.FragmentMapBinding

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "binding is null"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            findNavController().navigate(R.id.permissionFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

}