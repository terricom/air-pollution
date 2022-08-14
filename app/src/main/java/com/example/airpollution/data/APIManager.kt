package com.example.airpollution.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiManager {

    @GET("aqx_p_432")
    suspend fun getRecords(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("format") format: String = jsonFormat,
        @Query("api_key") key: String = apiKey
    ): Response<RecordData>

    companion object {

        private const val baseUrl = "https://data.epa.gov.tw/api/v2/"
        private const val jsonFormat = "json"
        private const val apiKey = "8ef419e1-cfba-489b-a8bf-620632c37a1f"

        fun create(): ApiManager {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient().newBuilder().apply {
                    val interceptor = HttpLoggingInterceptor()
                    interceptor.level = HttpLoggingInterceptor.Level.BODY
                    connectTimeout(120, TimeUnit.SECONDS)
                    readTimeout(120, TimeUnit.SECONDS)
                    writeTimeout(120, TimeUnit.SECONDS)
                    addInterceptor(interceptor)
                }.build())
                .build()
                .create(ApiManager::class.java)
        }
    }
}