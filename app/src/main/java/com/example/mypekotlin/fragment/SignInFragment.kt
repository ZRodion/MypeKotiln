package com.example.mypekotlin.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mypekotlin.MainActivity
import com.example.mypekotlin.R
import com.example.mypekotlin.databinding.FragmentSignInBinding
import com.example.mypekotlin.viewModel.SignInViewModel
import kotlinx.coroutines.launch


class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "binding is null"
        }
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            signUpButton.setOnClickListener {
                findNavController().navigate(R.id.signUpFragment)
            }
            signInButton.setOnClickListener {
                hideKeyboard()

                viewLifecycleOwner.lifecycleScope.launch {
                    switchVisibility()
                    val isNull = viewModel.signInUser(
                        binding.loginEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    )
                    if (isNull) {
                        switchVisibility()
                        binding.loginTextInputLayout.error =
                            getString(R.string.non_existent_account_error)
                    } else {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            loginEditText.doOnTextChanged { text, _, _, _ ->
                text?.let{
                    binding.signInButton.isEnabled = (it.isNotEmpty() && binding.passwordEditText.text.toString().isNotEmpty())
                }
            }
            passwordEditText.doOnTextChanged { text, _, _, _ ->
                text?.let{
                    binding.signInButton.isEnabled = (it.isNotEmpty() && binding.loginEditText.text.toString().isNotEmpty())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun switchVisibility() {
        binding.apply {
            val v = progressBar.visibility
            progressBar.visibility = linearLayout.visibility
            linearLayout.visibility = v
        }
    }
    private fun hideKeyboard(){
        val imm: InputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        view?.let { imm.hideSoftInputFromWindow(it.windowToken, 0) }
    }
}