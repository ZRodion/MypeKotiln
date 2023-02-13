package com.example.mypekotlin.viewModel

import androidx.lifecycle.ViewModel
import com.example.mypekotlin.PreferencesRepository
import com.example.mypekotlin.UserRepository
import com.example.mypekotlin.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class SignUpViewModel : ViewModel() {
    private val userRepository = UserRepository.get()
    private val preferencesRepository = PreferencesRepository.get()

    suspend fun signUpUser(login: String, password: String, name: String = "", surname: String = "") = withContext(Dispatchers.Default){
        if(!userRepository.isUserExist(login)){
            val id = UUID.randomUUID()
            userRepository.addUser(
                User(
                    id = id,
                    login = login,
                    password = password,
                    name = name,
                    surname = surname
                )
            )
            preferencesRepository.setStoredId(id)
            false
        }else{
            true
        }
    }
}