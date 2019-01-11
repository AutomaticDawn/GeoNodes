package com.example.vladimir.geonodes.Utilities

import org.json.JSONObject
import com.example.vladimir.geonodes.Utilities.locData

class locResponse(json: String) : JSONObject(json) {
    val count: String? = this.optString("count")
    val locations = this.optJSONArray("locations")
        ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } } // returns an array of JSONObject
        ?.map { locData(it.toString()) } // transforms each JSONObject of the array into locData
}