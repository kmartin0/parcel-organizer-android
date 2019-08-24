package com.km.parceltracker.api

import com.google.gson.annotations.SerializedName

// TODO: add details (TargetError)
data class ApiError(
    @SerializedName("error") val error: String,
    @SerializedName("error_description") val description: String,
    @SerializedName("code") val code: Int
) {
    companion object {
        const val TOKEN_EXPIRED = "invalid_token"
        const val ALREADY_EXISTS = "ALREADY_EXISTS"
        const val INTERNAL_SERVER_ERROR = "INTERNAL"
        const val INVALID_GRANT = "invalid_grant"
    }
}