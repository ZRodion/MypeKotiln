package com.example.mypekotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.mypekotlin.databinding.ActivityMainBinding
import com.example.mypekotlin.model.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        setupWithNavController(binding.bottomNav, navHostFragment.navController)

        lifecycleScope.launch {
            PreferencesRepository.get().storedLanguagePosition.collect{position ->
                val array = resources.getStringArray(R.array.languages)
                setLocale(array[position])
            }
        }
    }

    private fun setLocale(language: String) {
        val configuration = resources.configuration

        val locale = when(language){
            "English" -> "en"
            "Russian" -> "ru"
            else -> "en"
        }
        if(Locale(locale) != configuration.locale){
            configuration.locale = Locale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
            onConfigurationChanged(configuration)
            recreate()
        }
    }
}