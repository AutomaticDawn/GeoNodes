package com.example.vladimir.geonodes

import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.vladimir.geonodes.GPS_LATITUDE
import com.example.vladimir.geonodes.GPS_LONGITUDE

class mapScreen : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var gpsLatitude: Double = 0.0
    private var gpsLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_screen)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        gpsLatitude= getIntent().getDoubleExtra(GPS_LATITUDE, 0.0)
        gpsLongitude= getIntent().getDoubleExtra(GPS_LONGITUDE, 0.0)
        Log.d("CodeAndroidLocation", "Opened Map View")
        Log.d("CodeAndroidLocation", "-Map Latitude : " + gpsLatitude)
        Log.d("CodeAndroidLocation", "-Map Longitude : " + gpsLongitude)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val currentLocation = LatLng(gpsLatitude,gpsLongitude)
        mMap.addMarker(MarkerOptions().position(currentLocation).title("Your Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 16.0f ) );
    }
}
