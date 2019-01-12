package com.example.vladimir.geonodes.Visual

import android.Manifest
import android.annotation.SuppressLint
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.example.vladimir.geonodes.R
import kotlinx.android.synthetic.main.activity_entry_screen.*
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.vladimir.geonodes.Utilities.locData
import com.example.vladimir.geonodes.Utilities.locResponse

class EntryScreen : AppCompatActivity() {

    val REQUEST_LOCATION = 1 // Obavezno promeniti i dodati ovo u konstante !!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_screen)
        disableView()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) // Proverava Permisson
        {
            // Nemamo Permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_LOCATION) // Traži Permission
            enableView()
        }
        else
        {
            enableView()
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR) // Zabranjuje okretanje ekrana
    }

    fun disableView() //Vidljivost Dugme
    {

    }
    fun enableView()
    {
        Toast.makeText(this, "Imamo Permission", Toast.LENGTH_SHORT).show()
    }

    fun startThread(view: View)
    {
        val bck_intent = Intent(this, MyIntentService::class.java)
        Log.d("Bck_Service","Starting Background Service")
        startService(bck_intent)
    }
}

