package com.example.mypekotlin

import android.app.Application

class MypeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        UserRepository.initialize(this)
        PreferencesRepository.initialize(this)
    }
}