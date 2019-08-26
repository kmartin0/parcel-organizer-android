package com.km.parcelorganizer.api

import com.km.parcelorganizer.api.request.RegisterParcelRequestBody
import com.km.parcelorganizer.api.request.RegisterUserRequestBody
import com.km.parcelorganizer.api.request.UpdateParcelRequestBody
import com.km.parcelorganizer.api.request.UpdateUserRequestBody
import com.km.parcelorganizer.enums.ParcelStatusEnum
import com.km.parcelorganizer.model.OAuth2Credentials
import com.km.parcelorganizer.model.Parcel
import com.km.parcelorganizer.model.ParcelStatus
import com.km.parcelorganizer.model.User
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