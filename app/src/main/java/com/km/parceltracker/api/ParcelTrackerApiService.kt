package com.km.parceltracker.api

import com.km.parceltracker.model.Authorization
import com.km.parceltracker.model.Parcel
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface ParcelTrackerApiService {

    @GET("/parcels")
    fun getParcels(): Single<List<Parcel>>

    @POST("/oauth/token")
    fun authenticateUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password"
    ): Single<Authorization>
}