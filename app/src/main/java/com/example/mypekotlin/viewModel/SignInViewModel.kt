package com.example.mypekotlin.viewModel

import androidx.lifecycle.ViewModel
import com.example.mypekotlin.PreferencesRepository
import com.example.mypekotlin.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class SignInViewModel : ViewModel() {
    private val userRepository = UserRepository.get()
    private val preferencesRepository = PreferencesRepository.get()

    suspend fun signInUser(login: String, password: String) = withContext(Dispatchers.IO) {
        delay(2000)
        val user = userRepository.getUserByLogin(login, password)
        if (user != null) {
            preferencesRepository.setStoredId(user.id)
            false
        }else{
            true
        }
    }

}