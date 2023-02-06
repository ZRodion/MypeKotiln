package com.example.mypekotlin.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mypekotlin.R
import com.example.mypekotlin.databinding.FragmentSettingsBinding
import com.example.mypekotlin.viewModel.SettingsViewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "binding is null"
        }

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.languageSpinner.adapter = adapter
            binding.languageSpinner.setSelection(viewModel.position)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.languageSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(viewModel.position != position){
                    viewModel.setArrayPosition(position)
                }
                /*binding.languageChangeText.setText(R.string.change_language_text_view)
                binding.sendButton.setText(R.string.send_email_button)*/
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.googlePlayButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps")))
        }
        binding.sendButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND);


            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("1234@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));

            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}