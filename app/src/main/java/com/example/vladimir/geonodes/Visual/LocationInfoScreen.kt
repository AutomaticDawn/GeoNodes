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

class LocationInfoScreen : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("JSON_Server", "LocationInfoScreen On Create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_info_screen)
        val temp = getIntent().getStringExtra("locList")
        var id = getIntent().getIntExtra("id", 0)
        val locList: locResponse = locResponse(temp)
        loadLocation(locList,id)
    }

    fun loadLocation(locList: locResponse, i: Int)
    {
        // DODATI STVARI IZ BAZE PODATAKA
        Log.d("JSON_Server", "LocationInfoScreen loadLocation")
        locationNameValue.text = locList.locations!![i].name
        latitudeValue.text = locList.locations!![i].latitude.toString()
        longitudeValue.text = locList.locations!![i].longitude.toString()

    }

}
