package com.example.vladimir.geonodes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_location_info_screen.*

class LocationInfoScreen : AppCompatActivity() {

    public var name = "addName"
    private var locationName: String = "addLocationName"
    private var locationDescription: String = "addLocationDescription"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_info_screen)
    }

    fun loadLocation()
    {
        // DODATI STVARI IZ BAZE PODATAKA

        locationTitle.text = locationName
        description.text = locationDescription

    }

}
