package com.example.mypekotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypekotlin.R
import com.example.mypekotlin.databinding.FragmentPermissionBinding

class PermissionFragment : Fragment() {

    private var _binding: FragmentPermissionBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "binding is null"
        }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                findNavController().navigate(R.id.mapFragment)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.requestPermissionButton.setOnClickListener {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}