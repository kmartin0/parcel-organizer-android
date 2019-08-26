package com.km.parceltracker.api

import com.km.parceltracker.api.request.RegisterParcelRequestBody
import com.km.parceltracker.api.request.RegisterUserRequestBody
import com.km.parceltracker.api.request.UpdateParcelRequestBody
import com.km.parceltracker.api.request.UpdateUserRequestBody
import com.km.parceltracker.enums.ParcelStatusEnum
import com.km.parceltracker.model.OAuth2Credentials
import com.km.parceltracker.model.Parcel
import com.km.parceltracker.model.ParcelStatus
import com.km.parceltracker.model.User
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface ParcelTrackerApiService {

    @POST(Endpoints.PARCELS)
    fun saveParcel(@Body registerParcelRequestBody: RegisterParcelRequestBody): Single<Parcel>

    @GET(Endpoints.PARCELS)
    fun getParcels(): Single<List<Parcel>>

    @PUT(Endpoints.PARCELS)
    fun updateParcel(@Body updateParcelRequestBody: UpdateParcelRequestBody): Single<Parcel>

    @DELETE(Endpoints.DELETE_PARCEL)
    fun deleteParcel(@Path("id") id: Long): Completable

    @GET(Endpoints.GET_PARCEL_STATUS_BY_STATUS)
    fun getParcelStatusByStatus(@Path("status") parcelStatusEnum: ParcelStatusEnum): Single<ParcelStatus>

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
    fun registerUser(@Body registerUserRequestBody: RegisterUserRequestBody): Single<User>

    @PUT(Endpoints.USERS)
    fun updateUser(@Body updateUserRequestBody: UpdateUserRequestBody) : Single<User>
}