package com.example.vladimir.geonodes.Use_later

import android.util.Log
import android.app.Application

class App : Application() {
    companion object {
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        Log.d("JSON_Server", "prefs")
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}