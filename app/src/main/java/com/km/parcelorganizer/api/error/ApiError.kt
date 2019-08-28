package com.km.parcelorganizer.api.error

import com.google.gson.annotations.SerializedName
import com.km.parcelorganizer.enums.ApiErrorEnum

data class ApiError(
    @SerializedName("error") val error: ApiErrorEnum,
    @SerializedName("error_description") val description: String,
    @SerializedName("code") val code: Int,
    @SerializedName("details") val details : Array<TargetError>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApiError

        if (error != other.error) return false
        if (description != other.description) return false
        if (code != other.code) return false
        if (details != null) {
            if (other.details == null) return false
            if (!details.contentEquals(other.details)) return false
        } else if (other.details != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = error.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + code
        result = 31 * result + (details?.contentHashCode() ?: 0)
        return result
    }
}