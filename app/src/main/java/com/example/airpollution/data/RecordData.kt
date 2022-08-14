package com.example.airpollution.data

data class RecordData (val total: Int,
                       val limit: Int,
                       val offset: Int,
                       val records: List<Record>)