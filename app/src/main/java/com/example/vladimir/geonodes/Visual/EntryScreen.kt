package com.example.vladimir.geonodes.Visual

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
    var CHANNEL_ID = "1" // Obavezno promeniti i dodati ovo u konstante !!!
    val GPS_LATITUDE = "latitude" // Obavezno promeniti i dodati ovo u konstante !!!
    val GPS_LONGITUDE = "longitude" // Obavezno promeniti i dodati ovo u konstante !!!
    val URL_REGISTER = "https://geonodesapi.herokuapp.com/api/Todo/"

    lateinit var locationManager: LocationManager
    private var hasGPS = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null
    private var locationMain: Location? = null

    var locTemp: locResponse? = null

    public var locList: String? = null
    var HasDatabase: Boolean = false

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
        locTest.isEnabled=false
        locTest.alpha=0.5F
        locationButton.setOnClickListener { location()}
        Toast.makeText(this, "Imamo Permission", Toast.LENGTH_SHORT).show()
    }

    fun openMapView(view: View) // Funkcija koja otvara mapu
    {
        var mapIntent = Intent(this, mapScreen::class.java)
        mapIntent.putExtra(GPS_LATITUDE,locationMain!!.latitude)
        mapIntent.putExtra(GPS_LONGITUDE,locationMain!!.longitude)
        mapIntent.putExtra("title","Your Location")
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
                Log.d("CodeAndroidLocation", "GPS available")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                locationGps = location
                                locationMain = locationGps
                                Log.d("CodeAndroidLocation", "Updated GPS Coordinates") //Default koordinate su GPS koordinate
                                writeCoordinates()
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

            if (hasNetwork) //Network -------------------------------------------------------
            {
                Log.d("CodeAndroidLocation", "Network available")
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                locationNetwork = location
                                Log.d("CodeAndroidLocation", "Updated Network Coordinates")
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
                    Log.d("CodeAndroidLocation", "Main location is now Network location")
                }
                else
                {
                    locationMain = locationGps
                    Log.d("CodeAndroidLocation", "Main location is now GPS location")
                }
                writeCoordinates()
                Log.d("CodeAndroidLocation", "Main Latitude : " + locationMain!!.latitude)
                Log.d("CodeAndroidLocation", "Main Longitude : " + locationMain!!.longitude)

            }
        }
        else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    fun writeCoordinates()
    {
        geoSirina.text = "Širina: " + locationMain!!.latitude
        geoDuzina.text = "Dužina: " + locationMain!!.longitude

        Log.d("CodeAndroidLocation", "Written Latitude : " + locationMain!!.latitude)
        Log.d("CodeAndroidLocation", "Written Longitude : " + locationMain!!.longitude)

        openMapButton.isEnabled=true
        openMapButton.alpha=1F
    }

    fun serverRequest(view: View)
    {
        Log.d("JSON_Server", "Tried")
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val objRequest: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            "http://89.216.137.159:3000/locations",
            null,
            Response.Listener { response ->
                    Log.d("JSON_Server", "Connected")
                    Log.d("JSON_Server", response.toString())
                    testView.text = "Status: 200 Ok"
                    locList = response.toString()
                    HasDatabase = true
                    locTest.isEnabled=true
                    locTest.alpha=1F
                    locTemp = locResponse(response.toString())
            },
            Response.ErrorListener {
                    Log.d("JSON_Server", "Not Connected")
                    testView.text = "Status: 404 Not Found"
                    //Log.d("JSON_Server", error.toString())
            }
        )
        requestQueue.add(objRequest)
    }

    fun locTest(view: View)
    {
        if(HasDatabase)
        {
            var locId = locationId.text.toString()
            var id = locId.toInt()
            if(locationId.text != null && id >=0 && id < locTemp!!.count && locationId.text != null) {
                Log.d("JSON_Server", "LocationInfoScreen Prep")
                var locationIntent = Intent(this, LocationInfoScreen::class.java)
                locationIntent.putExtra("locList", locList)
                locationIntent.putExtra("id", id)
                startActivity(locationIntent)
            }
            else{
                Toast.makeText(this, "Nevažeći id", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

