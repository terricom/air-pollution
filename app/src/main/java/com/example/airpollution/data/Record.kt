package com.example.airpollution.data

import com.google.gson.annotations.SerializedName

data class Record (@SerializedName("sitename") val siteName: String,
                   @SerializedName("county") val county: String,
                   @SerializedName("status") val status: String,
                   @SerializedName("pm2.5") val pmValue: String,
                   @SerializedName("siteid") val siteId: String)