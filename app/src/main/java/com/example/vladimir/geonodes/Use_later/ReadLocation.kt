package com.example.vladimir.geonodes.Use_later

import android.util.Log
import org.json.JSONObject
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

object ReadLocation {
    val URL_REGISTER = "https://geonodesapi.herokuapp.com/api/Todo/"
    fun SendLocation(Latitude: Double, Longitude: Double, complete: (Boolean) -> Unit)
    {
        Log.d("JSON_Server", "Tried")
        val jsonBody
        = JSONObject()

        jsonBody.put("latitude", Latitude)
        jsonBody.put("longitude", Longitude)
        val requestBody = jsonBody.toString()

        val sendLocationRequest = object : StringRequest(Method.POST,
            URL_REGISTER, Response.Listener { response ->
            complete(true)
            Log.d("JSON_Server", "Connected")
        }, Response.ErrorListener { error ->
            complete(false)
            Log.d("JSON_Server", "Could not send location: $error")
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Log.d("JSON_Server", "Making Request")

        //Pitati
        App.prefs.requestQueue.add(sendLocationRequest)
        Log.d("JSON_Server", "Making Request 2")
    }
}