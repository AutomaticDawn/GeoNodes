package com.example.vladimir.geonodes.Visual

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.vladimir.geonodes.R
import com.example.vladimir.geonodes.Utilities.locData
import kotlinx.android.synthetic.main.activity_location_info_screen.*
import com.example.vladimir.geonodes.Utilities.*
import com.example.vladimir.geonodes.Visual.*
import com.google.gson.JsonObject
import org.json.JSONObject
import android.content.Intent
import android.net.Uri
import android.view.View


class LocationInfoScreen : AppCompatActivity() {

    var locList: locResponse? = null
    var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("JSON_Server", "LocationInfoScreen On Create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_info_screen)
        val temp = getIntent().getStringExtra("locList")
        id = getIntent().getIntExtra("id", 0)
        locList= locResponse(temp)
        loadLocation()
    }

    fun loadLocation()
    {
        // DODATI STVARI IZ BAZE PODATAKA
        locationNameValue.text = locList!!.locations!![id].name
        latitudeValue.text = locList!!.locations!![id].latitude.toString()
        longitudeValue.text = locList!!.locations!![id].longitude.toString()
    }

    fun redirectClick(view: View)
    {
        Log.d("JSON_Server", "Opening: " + locList!!.locations!![id].name)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(locList!!.locations!![id].url))
        startActivity(browserIntent)
    }

    fun mapOpener(view: View) // Funkcija koja otvara mapu
    {
        Log.d("JSON_Server", "Opening map")
        var mapIntent = Intent(this, mapScreen::class.java)
        Log.d("JSON_Server", "Created Intent")
        mapIntent.putExtra("latitude",locList!!.locations!![id].latitude)
        mapIntent.putExtra("longitude",locList!!.locations!![id].longitude)
        mapIntent.putExtra("title",locList!!.locations!![id].name)
        Log.d("JSON_Server", "Put Extras")
        startActivity(mapIntent)
    }
}
