package com.example.mypekotlin.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypekotlin.PreferencesRepository
import com.example.mypekotlin.UserRepository
import com.example.mypekotlin.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val preferencesRepository = PreferencesRepository.get()
    private val userRepository = UserRepository.get()

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user: StateFlow<User?>
        get() = _user.asStateFlow()

    init {
        viewModelScope.launch {
            val id = preferencesRepository.storedId.first()
            Log.d("MyTag", id.toString())
            userRepository.getUserById(id!!).collect{ user ->
                _user.value = user
            }
        }
    }

    fun updateUser(onUpdate: (User) -> User) {
        _user.update { user ->
            user?.let { onUpdate(it) }
        }
        user.value?.let { userRepository.updateUser(it) }
    }
}