package com.example.mypekotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.mypekotlin.R
import com.example.mypekotlin.databinding.InputDialogBinding
import com.example.mypekotlin.model.User
import com.example.mypekotlin.viewModel.InputDialogViewModel
import kotlinx.coroutines.launch

class InputDialogFragment : DialogFragment(R.layout.input_dialog) {
    private var _binding: InputDialogBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "binding is null"
        }

    private val viewModel: InputDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = InputDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.user.collect{user ->
                    user?.let{updateUI(it)}
                }
            }
        }

        binding.apply {
            inputName.doOnTextChanged { text, _, _, _ ->
                viewModel.updateUser { oldUser ->
                    oldUser.copy(name = text.toString())
                }
            }
            inputSurname.doOnTextChanged { text, _, _, _ ->
                viewModel.updateUser { oldUser ->
                    oldUser.copy(surname = text.toString())
                }
            }
            inputInfo.doOnTextChanged { text, _, _, _ ->
                viewModel.updateUser { oldUser ->
                    oldUser.copy(personInformation = text.toString())
                }
            }

            saveButton.setOnClickListener {
                if(inputName.text.isEmpty()){
                   inputName.error = getString(R.string.edit_text_empty_error_message, "name")
                }else if(inputName.text.contains(" ")){
                    inputName.error = getString(R.string.edit_text_space_error_message, "name")
                }else if(inputSurname.text.isEmpty()){
                    inputSurname.error = getString(R.string.edit_text_empty_error_message, "surname")
                } else if(inputSurname.text.contains(" ")){
                    inputSurname.error = getString(R.string.edit_text_space_error_message, "surname")
                }else {
                    findNavController().popBackStack()
                }
            }
        }

    }

    private fun updateUI(user: User) {
        binding.apply {
            if(inputName.text.toString() != user.name){
                inputName.setText(user.name)
            }
            if(inputSurname.text.toString() != user.surname){
                inputSurname.setText(user.surname)
            }
            if(inputInfo.text.toString() != user.personInformation){
                inputInfo.setText(user.personInformation)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}