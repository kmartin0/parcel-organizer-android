package com.km.parceltracker.api

import com.km.parceltracker.model.Authorization
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RefreshTokenApiService {

    @POST("/oauth/token")
    @FormUrlEncoded
    fun refreshToken(@Field("grant_type") grantType: String = "refresh_token", @Field("refresh_token") refreshToken: String): Single<Authorization>

}