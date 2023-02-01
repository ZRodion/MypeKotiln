package com.example.mypekotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mypekotlin.databinding.FragmentMapBinding
import com.example.mypekotlin.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "binding is null"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
}