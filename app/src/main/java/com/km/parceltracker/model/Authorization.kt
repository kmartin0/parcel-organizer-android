package com.km.parceltracker.model

import com.google.gson.annotations.SerializedName

data class Authorization(
    @SerializedName("access_token") var accessToken: String,
    @SerializedName("token_type") var tokenType: String,
    @SerializedName("refresh_token") var refreshToken: String,
    @SerializedName("expires_in") var expiresIn: Long,
    @SerializedName("scope") var scope: String,
    @SerializedName("jti") var jti: String
)