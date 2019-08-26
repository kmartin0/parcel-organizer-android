package com.km.parcelorganizer.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OAuth2Credentials(
    @SerializedName("access_token") var accessToken: String,
    @SerializedName("token_type") var tokenType: String,
    @SerializedName("refresh_token") var refreshToken: String,
    @SerializedName("expires_in") var expiresIn: Long,
    @SerializedName("scope") var scope: String,
    @SerializedName("jti") var jti: String
) : Parcelable