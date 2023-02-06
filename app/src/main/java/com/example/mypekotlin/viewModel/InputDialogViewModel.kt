package com.example.mypekotlin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypekotlin.PreferencesRepository
import com.example.mypekotlin.UserRepository
import com.example.mypekotlin.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class InputDialogViewModel: ViewModel() {
    private val preferencesRepository = PreferencesRepository.get()
    private val userRepository = UserRepository.get()

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user: StateFlow<User?>
        get() = _user.asStateFlow()

    init {
        viewModelScope.launch {
            var id = preferencesRepository.storedId.first()
            if(id == null){
                val newId = UUID.randomUUID()
                userRepository.addUser(User(id = newId))
                preferencesRepository.setStoredId(newId)
                id = newId.toString()
            }

            userRepository.getUser(UUID.fromString(id)).collect{user ->
                _user.value = user
            }
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