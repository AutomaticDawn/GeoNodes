package com.example.vladimir.geonodes.Utilities

import org.json.JSONObject

class locData(json: String) : JSONObject(json) {
    val id: String? = this.optString("id")
    val name: String? = this.optString("name")
    val latitude: Double? = this.optDouble("latitude")
    val longitude: Double? = this.optDouble("longitude")
    val url: String? = this.optString("url")
}