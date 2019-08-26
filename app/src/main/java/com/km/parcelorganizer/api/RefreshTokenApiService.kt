package com.km.parcelorganizer.api

import com.km.parcelorganizer.model.OAuth2Credentials
import io.reactivex.Single
import retrofit2.http.*

interface RefreshTokenApiService {

    @POST(Endpoints.OAUTH_TOKEN)
    @FormUrlEncoded
    fun refreshToken(@Field("refresh_token") refreshToken: String, @Field("grant_type") grantType: String = "refresh_token"): Single<OAuth2Credentials>

}