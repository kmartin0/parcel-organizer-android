package com.km.parcelorganizer.api.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateUserRequestBody(
    val id: Long,
    val email: String,
    val name: String,
    @SerializedName("password") val currentPassword: String
) : Parcelable