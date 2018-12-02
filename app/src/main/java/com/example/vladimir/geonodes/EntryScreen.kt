package com.example.vladimir.geonodes

import android.Manifest
import android.annotation.SuppressLint
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
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_entry_screen.*

class EntryScreen : AppCompatActivity() {

    val REQUEST_LOCATION = 1 // Obavezno promeniti i dodati ovo u konstante !!!
    var CHANNEL_ID = "1" // Obavezno promeniti i dodati ovo u konstante !!!
    val GPS_LATITUDE = "latitude" // Obavezno promeniti i dodati ovo u konstante !!!
    val GPS_LONGITUDE = "longitude" // Obavezno promeniti i dodati ovo u konstante !!!

    lateinit var locationManager: LocationManager
    private var hasGPS = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    //private var locationNetwork: Location? = null

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
        locationButton.isEnabled = false
        locationButton.alpha = 0.5F
    }
    fun enableView()
    {
        locationButton.isEnabled = true
        locationButton.alpha = 1F
        openMapButton.isEnabled = false
        openMapButton.alpha = 0.5F
        locationButton.setOnClickListener { location()}
        Toast.makeText(this, "Imamo Permission", Toast.LENGTH_SHORT).show()
    }

    fun openMapView(view: View) // Funkcija koja otvara mapu
    {
        var mapIntent = Intent(this, mapScreen::class.java)
        mapIntent.putExtra(GPS_LATITUDE,locationGps!!.latitude)
        mapIntent.putExtra(GPS_LONGITUDE,locationGps!!.longitude)
        Log.d("CodeAndroidLocation", "Prosledjene koordinate:")
        Log.d("CodeAndroidLocation", "GPS Latitude : " + locationGps!!.latitude)
        Log.d("CodeAndroidLocation", "GPS Longitude : " + locationGps!!.longitude)
        startActivity(mapIntent)
    }

    fun notify(view: View) {
        val notIntent = Intent(this, EntryScreen::class.java) // Pravljenje intent-a za notifikaciju
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, notIntent, 0)

        var notifikacija = NotificationCompat.Builder(this, CHANNEL_ID) //Deklaracija notifikacije
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Notifikacija")
            .setContentText("Ovo radi")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1, notifikacija.build())
        }
    }

    @SuppressLint("MissingPermission")
    fun location() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (hasGPS || hasNetwork) {
            locationButton.isEnabled=false
            locationButton.alpha=0.5F

            if (hasGPS) //GPS -------------------------------------------------------------
            {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0f, object : LocationListener
                    {
                        override fun onLocationChanged(location: Location?) {
                            Log.d("CodeAndroidLocation", "Kordinate:")
                            if (location != null) {
                                locationGps = location
                                geoSirina.text = "Širina: " + locationGps!!.latitude
                                geoDužina.text = "Dužina: " + locationGps!!.longitude
                                Log.d("CodeAndroidLocation", "GPS Latitude : " + locationGps!!.latitude)
                                Log.d("CodeAndroidLocation", "GPS Longitude : " + locationGps!!.longitude)
                                openMapButton.isEnabled=true
                                openMapButton.alpha=1F
                            }
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                        }

                        override fun onProviderEnabled(provider: String?) {

                        }

                        override fun onProviderDisabled(provider: String?) {

                        }
                    })
                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null) {
                    locationGps = localGpsLocation
                }
            } //-------------------------------------------------------------

            /*if (hasNetwork) //Network -------------------------------------------------------
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object : LocationListener {

                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                locationNetwork = location
                                geoSirina.text = "Širina: " + locationNetwork!!.latitude
                                geoDužina.text = "Dužina: " + locationNetwork!!.longitude
                                Log.d("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
                                Log.d("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)
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
            }*/

            /*if (locationGps != null && locationNetwork != null) { // Ispisivanje lokacije na ekran
                if (locationGps!!.accuracy >= locationNetwork!!.accuracy) {
                    geoSirina.text = "Širina: " + locationGps!!.latitude
                    geoDužina.text = "Dužina: " + locationGps!!.longitude
                    Log.d("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
                    Log.d("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)
                } else {
                    geoSirina.text = "Širina: " + locationNetwork!!.latitude
                    geoDužina.text = "Dužina: " + locationNetwork!!.longitude
                    Log.d("CodeAndroidLocation", " GPS Latitude : " + locationNetwork!!.latitude)
                    Log.d("CodeAndroidLocation", " GPS Longitude : " + locationNetwork!!.longitude)
                }
            }*/
        }
        else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
}

