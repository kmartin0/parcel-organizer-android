package com.km.parceltracker.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ParcelTrackerApi {
    companion object {
        // The base url off the api.
        private const val baseUrl = "http://10.0.2.2:8080/"

        /**
         * @return [ParcelTrackerApi] The service class off the retrofit client.
         */
        fun createApi(): ParcelTrackerApiService {
            // Create an OkHttpClient to be able to make a log of the network traffic
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            // Create the Retrofit instance
            val numbersApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            // Return the Retrofit NumbersApiService
            return numbersApi.create(ParcelTrackerApiService::class.java)
        }
    }
}