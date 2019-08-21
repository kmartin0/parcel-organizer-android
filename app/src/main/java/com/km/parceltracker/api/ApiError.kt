package com.km.parceltracker.api

import com.google.gson.annotations.SerializedName

// TODO: add details (TargetError)
data class ApiError(
    @SerializedName("error") val error: String,
    @SerializedName("error_description") val description: String,
    @SerializedName("code") val code: Int
)