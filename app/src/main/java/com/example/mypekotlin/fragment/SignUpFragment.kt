package com.example.mypekotlin.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mypekotlin.MainActivity
import com.example.mypekotlin.R
import com.example.mypekotlin.databinding.FragmentSignUpBinding
import com.example.mypekotlin.viewModel.SignUpViewModel
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val isExist = viewModel.signUpUser(
                    binding.loginEditText.text.toString(),
                    binding.passwordEditText.text.toString(),
                    binding.nameEditText.text.toString(),
                    binding.surnameEditText.text.toString(),
                )
                if(isExist){
                    binding.loginTextInputLayout.error = getString(R.string.login_is_taken)
                }else{
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.signInFragment)
        }
        binding.loginEditText.doOnTextChanged { text, _, _, _ ->
            text?.let {
                binding.signUpButton.isEnabled =
                    checkLoginText(text, binding.passwordEditText.text.toString())
            }
        }
        binding.passwordEditText.doOnTextChanged { text, _, _, _ ->
            text?.let {
                binding.signUpButton.isEnabled = checkLoginText(
                    binding.loginEditText.text.toString(),
                    text
                )
            }
        }
    }

    private fun checkLoginText(login: CharSequence, password: CharSequence): Boolean {
        return (login.length <= 20 && login.isNotEmpty() && password.isNotEmpty() && password.length >= 8)
    }
}