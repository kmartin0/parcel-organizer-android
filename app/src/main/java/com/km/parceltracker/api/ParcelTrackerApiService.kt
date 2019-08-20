package com.km.parceltracker.api

import com.km.parceltracker.model.Parcel
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET

interface ParcelTrackerApiService {

    @GET("/parcels")
    fun getParcels(): Single<List<Parcel>>
}