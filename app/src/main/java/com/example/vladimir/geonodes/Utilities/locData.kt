package com.example.vladimir.geonodes.Utilities

import org.json.JSONObject

class locData(json: String) : JSONObject(json) {
    val id: String? = this.optString("id")
    val name: String? = this.optString("name")
    val latitude: String? = this.optString("latitude")
    val longitude: String? = this.optString("longitude")
}