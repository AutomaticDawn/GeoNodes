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

    lateinit var locationManager: LocationManager
    private var hasGPS = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null
    private var locationMain: Location? = null

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
            //location()
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

    /*@SuppressLint("MissingPermission")
    fun location() {
        Log.d("Bck_Service", "Finding Coordinates")
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (hasGPS || hasNetwork) {
            if (hasGPS) //GPS -------------------------------------------------------------
            {
                Log.d("Bck_Service", "GPS available but still not found")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, object :
                    LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationGps = location
                            locationMain = locationGps
                            Log.d("Bck_Service", "Found and updated GPS Coordinates") //Default koordinate su GPS koordinate
                            //writeCoordinates()
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        Log.d("Bck_Service","Status Changed for GPS")
                    }

                    override fun onProviderEnabled(provider: String?) {
                        Log.d("Bck_Service","Provider Enabled for GPS")
                    }

                    override fun onProviderDisabled(provider: String?) {
                        Log.d("Bck_Service","Provider Disabled for GPS")
                    }
                })
                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null) {
                    locationGps = localGpsLocation
                }
            } //-------------------------------------------------------------

            if (hasNetwork) //Network -------------------------------------------------------
            {
                Log.d("Bck_Service", "Network available but still not found")
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object :
                    LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationNetwork = location
                            Log.d("Bck_Service", "Found and updated Network Coordinates")
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }
                })
                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null) {
                    locationNetwork = localNetworkLocation
                }//-------------------------------------------------------------
            }

            if (locationGps != null || locationNetwork != null) // Ispisivanje lokacije na ekran
            {
                if (locationGps == null && locationNetwork != null)
                {
                    locationMain = locationNetwork
                    Log.d("Bck_Service", "Main location is now Network location")
                }
                else
                {
                    locationMain = locationGps
                    Log.d("Bck_Service", "Main location is now GPS location")
                }
                //writeCoordinates()
                Log.d("Bck_Service", "Main Latitude : " + locationMain!!.latitude)
                Log.d("Bck_Service", "Main Longitude : " + locationMain!!.longitude)

            }
        }
        else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }*/
}

