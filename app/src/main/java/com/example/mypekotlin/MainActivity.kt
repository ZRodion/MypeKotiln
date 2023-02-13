package com.example.mypekotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.mypekotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val position = runBlocking {
            PreferencesRepository.get().storedLanguagePosition.first()
        }
        setLocale(position)

        loginCheck()

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav, navHostFragment.navController)

        lifecycleScope.launch {
            PreferencesRepository.get().storedLanguagePosition.collect {
                //execute once at start
                if (it != position) {
                    setLocale(it)
                }
            }
        }
    }

    private fun setLocale(position: Int) {
        val array = resources.getStringArray(R.array.languages)
        val configuration = resources.configuration
        val locale = when (array[position]) {
            "English" -> "en"
            "Russian" -> "ru"
            else -> "en"
        }
        if (Locale(locale) != configuration.locale) {
            configuration.locale = Locale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
            onConfigurationChanged(configuration)
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    private fun loginCheck(){
        lifecycleScope.launch {
            PreferencesRepository.get().storedId.collect{id ->
                if(id == null){
                    loginIntent()
                }
            }
        }
    }
    private fun loginIntent(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }
}