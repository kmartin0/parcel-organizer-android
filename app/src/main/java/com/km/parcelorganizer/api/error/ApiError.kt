package com.km.parcelorganizer.api.error

import com.google.gson.annotations.SerializedName
import com.km.parcelorganizer.enums.ApiErrorEnum

data class ApiError(
    @SerializedName("error") val error: ApiErrorEnum,
    @SerializedName("error_description") val description: String,
    @SerializedName("code") val code: Int,
    @SerializedName("details") val details : HashMap<String, String>?
)