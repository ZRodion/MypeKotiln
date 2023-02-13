package com.example.mypekotlin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypekotlin.PreferencesRepository
import kotlinx.coroutines.launch

class SettingsViewModel: ViewModel() {
    private val preferencesRepository = PreferencesRepository.get()

    var position: Int = 0

    init {
        viewModelScope.launch {
            preferencesRepository.storedLanguagePosition.collect{
                position = it
            }
        }
    }

    fun setArrayPosition(pos: Int){
        viewModelScope.launch {
            preferencesRepository.setLanguagePosition(pos)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            preferencesRepository.setStoredId(null)
        }
    }
}