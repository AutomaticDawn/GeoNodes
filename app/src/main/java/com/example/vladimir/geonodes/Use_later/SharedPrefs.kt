package com.example.vladimir.geonodes.Use_later


import android.content.Context
import android.content.SharedPreferences
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class SharedPrefs(context: Context)
{
    val PREFS_FILENAME = "prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    val LATITUDE: String = "latitude"
    val LONGITUDE: String = "longitude"


    var Latitude: Float
        get() = prefs.getFloat(LATITUDE, 0.0f)
        set(value) = prefs.edit().putFloat(LATITUDE, value).apply()

    var Longitude: Float
        get() = prefs.getFloat(LONGITUDE, 0.0f)
        set(value) = prefs.edit().putFloat(LONGITUDE, value).apply()


    val requestQueue: RequestQueue = Volley.newRequestQueue(context)
}