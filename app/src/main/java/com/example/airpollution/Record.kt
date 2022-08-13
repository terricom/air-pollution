package com.example.airpollution

data class Record (val siteName: String,
                   val county: String,
                   val status: String,
                   val pmValue: Int,
                   val siteId: String)