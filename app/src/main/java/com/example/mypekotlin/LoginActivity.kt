package com.example.mypekotlin

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val onBackPressedCallback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Snackbar.make(findViewById(R.id.login_nav_host_fragment), getString(R.string.login_or_register), Snackbar.LENGTH_LONG).show()
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    fun example(){

    }
}