package com.example.vladimir.geonodes.Visual

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.vladimir.geonodes.R
import kotlinx.android.synthetic.main.activity_entry_screen.*

class EntryScreen : AppCompatActivity() {

    val REQUEST_LOCATION = 1 // Obavezno promeniti i dodati ovo u konstante !!!

    companion object {
        var exploring = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_screen)
        disableView()
        checkPermissions()
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR) // Zabranjuje okretanje ekrana
    }

    fun checkPermissions()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) // Proverava Permisson
        {
            // Nemamo Permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_LOCATION) // Traži Permission
            checkPermissions()
        }
        else
        {
            enableView()
        }
    }

    fun disableView() //Vidljivost Dugme
    {

    }
    fun enableView()
    {
        //Toast.makeText(this, "Imamo Permission", Toast.LENGTH_SHORT).show()
        readyText.text = "Everything is ready."
        stopThread.isEnabled = false
        stopThread.alpha = 0.5f
    }

    fun startThread(view: View)
    {
        EntryScreen.exploring = true
        val bck_intent = Intent(this, MyIntentService::class.java)
        Log.d("Bck_Service","Starting Background Service")
        startService(bck_intent)
        Thread.sleep(500)
        startThread.isEnabled = false
        startThread.alpha = 0.5f
        stopThread.isEnabled = true
        stopThread.alpha = 1f
    }

    fun stopThread(view: View)
    {
        EntryScreen.exploring = false
        val bck_intent = Intent(this, MyIntentService::class.java)
        Log.d("Bck_Service","Stoping Background Service")
        stopService(bck_intent)
        Thread.sleep(500)
        startThread.isEnabled = true
        startThread.alpha = 1f
        stopThread.isEnabled = false
        stopThread.alpha = 0.5f
    }

    fun exitApp (view: View)
    {
        val bck_intent = Intent(this, MyIntentService::class.java)
        Log.d("Bck_Service","Exiting The App")
        stopService(bck_intent)
        finish()
        System.exit(0)
    }


}

