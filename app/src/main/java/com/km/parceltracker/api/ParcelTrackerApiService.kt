package com.km.parceltracker.api

import com.km.parceltracker.api.request.RegisterParcelRequestBody
import com.km.parceltracker.api.request.RegisterRequestBody
import com.km.parceltracker.enums.ParcelStatusEnum
import com.km.parceltracker.model.OAuth2Credentials
import com.km.parceltracker.model.Parcel
import com.km.parceltracker.model.ParcelStatus
import com.km.parceltracker.model.User
import com.km.parceltracker.util.Endpoints
import io.reactivex.Single
import retrofit2.http.*

interface ParcelTrackerApiService {

    @GET(Endpoints.PARCELS)
    fun getParcels(): Single<List<Parcel>>

    @POST(Endpoints.PARCELS)
    fun saveParcel(@Body registerParcelRequestBodyBody: RegisterParcelRequestBody): Single<Parcel>

    @POST(Endpoints.OAUTH_TOKEN)
    @FormUrlEncoded
    fun authenticateUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password"
    ): Single<OAuth2Credentials>

    @GET(Endpoints.USERS)
    fun getUser(@Header("Authorization") accessToken: String): Single<User>

    @POST(Endpoints.USERS)
    fun registerUser(@Body registerRequestBody: RegisterRequestBody): Single<User>

    @GET(Endpoints.GET_PARCEL_STATUS_BY_STATUS)
    fun getParcelStatusByStatus(@Path("status") parcelStatusEnum: ParcelStatusEnum): Single<ParcelStatus>
}