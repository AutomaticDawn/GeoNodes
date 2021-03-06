package com.example.vladimir.geonodes.Visual

import android.annotation.SuppressLint
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.vladimir.geonodes.R
import com.example.vladimir.geonodes.Utilities.locResponse
import kotlinx.android.synthetic.main.activity_entry_screen.*
import java.lang.Math.abs
import kotlin.math.sqrt
import java.util.*
import kotlinx.android.synthetic.main.activity_entry_screen.*
import java.io.*


class MyIntentService : IntentService("MyIntentService") {

    private val FILE_NAME: String = "backup_database.txt"
    //var file = File(FILE_NAME)

    var time: Int = 0
    var runBck: Boolean = true
    var HasDatabase = false
    var locList: locResponse? = null
    var diff: Double = 0.0007 // Razlika
    var bannedId: Int = -1
    var bckup_db: String = "Hello World!"

    lateinit var locationManager: LocationManager
    private var hasGPS = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null
    private var locationMain: Location? = null

    companion object {
        var identification : Int = 1
    }

    override fun onHandleIntent(intent: Intent?) {
        //runBck = getIntent().getBooleanExtra("bckService", false)
        runBck = true
        serverRequest()
        Thread.sleep(1000)
        bckProcess()
    }

    fun bckProcess()
    {
        Log.d("Bck_Service", "Background Process Started")
        if(HasDatabase) {
            while (runBck && EntryScreen.exploring) {
                location()
                time++
                Log.d("Bck_Service", "Background Service running for: " + time + "s")
                if(time%5 == 0) compare()
                Thread.sleep(1000)
                if (time >= 3600){
                    break
                }
            }
        }
    }

    fun serverRequest()
    {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val objRequest: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            "https://geonodes.herokuapp.com/locations",
            null,
            Response.Listener { response ->
                Log.d("Bck_Service", "Background Service connected to database")
                Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
                locList = locResponse(response.toString())
                bckup_db = response.toString()
                HasDatabase = true
                backup()
            },
            Response.ErrorListener {
                Log.d("Bck_Service", "Background Service didn't connect")
                Toast.makeText(this, "Not Connected, working offline", Toast.LENGTH_SHORT).show()
                backup()
            }
        )
        requestQueue.add(objRequest)
    }

    fun backup()
    {
        if(HasDatabase)
        {
            Log.d("FilesManagement", "Writing to a file")
            val file = File(this.filesDir, FILE_NAME)
            val fileContents = bckup_db
            this.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
                it.write(fileContents.toByteArray())
            }
            if(file.exists())Log.d("FilesManagement", "File Exists")
            else Log.d("FilesManagement", "File Does Not Exist")
        }
        else
        {
            Log.d("FilesManagement", "Reading from a file")

            var fileInputStream: FileInputStream? = null
            fileInputStream = openFileInput(FILE_NAME)
            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            bckup_db = stringBuilder.toString()

            Log.d("FilesManagement", bckup_db)
            locList = locResponse(bckup_db)
            HasDatabase = true
        }
    }

    @SuppressLint("MissingPermission")
    fun location() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (hasGPS || hasNetwork) {
            if (hasGPS) //GPS -------------------------------------------------------------
            {
                //Log.d("Bck_Service", "GPS available but still not found")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, object :
                    LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationGps = location
                            locationMain = locationGps
                            //Log.d("Bck_Service", "Found and updated GPS Coordinates") //Default koordinate su GPS koordinate
                            //writeCoordinates()
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
                //Log.d("Bck_Service", "Network available but still not found")
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object :
                    LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationNetwork = location
                            //Log.d("Bck_Service", "Found and updated Network Coordinates")
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
                    //Log.d("Bck_Service", "Main location is now Network location")
                }
                else
                {
                    locationMain = locationGps
                    //Log.d("Bck_Service", "Main location is now GPS location")
                }
                //writeCoordinates()
                Log.d("Bck_Service", "Main Latitude : " + locationMain!!.latitude)
                Log.d("Bck_Service", "Main Longitude : " + locationMain!!.longitude)

            }
        }
        else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    fun compare()
    {
        var tempLatDiff: Double? = 0.0
        var tempLongDiff: Double? = 0.0
        var currDiff: Double? = 0.0
        if(locationMain?.latitude != null && locationMain?.longitude != null)
        {
            Log.d("Bck_Service", "Background Service is comparing with database")
            for(i in 0..locList!!.count-1)
            {
                //Log.d("Bck_Service", "Loop: " + i)
                tempLatDiff = locList!!.locations!![i].latitude!! - locationMain!!.latitude!!
                tempLongDiff = locList!!.locations!![i].longitude!! - locationMain!!.longitude!!
                currDiff = sqrt((tempLatDiff * tempLatDiff) + (tempLongDiff * tempLongDiff))
                //Log.d("Bck_Service", "Difference " + i + ": " + currDiff)
                if (currDiff < diff && bannedId != i) {
                    Log.d("Bck_Service", "Sending Notification with id: " + i + " <--")
                    notifyUser(i)
                    bannedId=i
                    //runBck = false
                    break
                }
            }
        }
        else
        {
            Toast.makeText(this, "No GPS Data", Toast.LENGTH_SHORT).show()
            Log.d("Bck_Service", "Background Service has no GPS data")
        }
    }

    fun notifyUser(id: Int) {
        val notIntent = Intent(this, LocationInfoScreen::class.java) // Pravljenje intent-a za notifikaciju
        notIntent.putExtra("locList", locList.toString())
        MyIntentService.Companion.identification = id
        //notIntent.putExtra("id", id)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 1, notIntent, 0)

        var notifikacija = NotificationCompat.Builder(this, "1") //Deklaracija notifikacije
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("You Are Near An Important Location")
            .setContentText(locList!!.locations!![id].name)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1, notifikacija.build())
        }
    }
}