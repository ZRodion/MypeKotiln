package com.example.mypekotlin.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypekotlin.api.RetrofitRepository
import com.example.mypekotlin.model.Marker
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel : ViewModel() {

    private val retrofitRepository = RetrofitRepository()

    private val _markers: MutableStateFlow<List<Marker>> =
        MutableStateFlow(emptyList())
    val markers: StateFlow<List<Marker>>
        get() = _markers.asStateFlow()

    init {

        viewModelScope.launch {
            try {
                _markers.value = retrofitRepository.fetchMarkers()
            } catch (e: Exception) {
                Log.e("MyTag", "Failed to fetch markers items", e)
            }
        }
    }
}