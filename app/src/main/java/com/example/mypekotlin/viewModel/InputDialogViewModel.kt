package com.example.mypekotlin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypekotlin.PreferencesRepository
import com.example.mypekotlin.UserRepository
import com.example.mypekotlin.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class InputDialogViewModel: ViewModel() {
    private val preferencesRepository = PreferencesRepository.get()
    private val userRepository = UserRepository.get()

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user: StateFlow<User?>
        get() = _user.asStateFlow()

    init {
        viewModelScope.launch {
            _user.value = userRepository.getUser(preferencesRepository.storedId.first()).first()
        }
    }
    fun updateUser(onUpdate: (User) -> User) {
        _user.update { oldUser ->
            oldUser?.let { onUpdate(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        user.value?.let { userRepository.updateUser(it) }
    }
}