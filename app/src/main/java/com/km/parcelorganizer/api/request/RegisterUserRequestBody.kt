package com.km.parcelorganizer.api.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterUserRequestBody(
    val email: String,
    val name: String,
    val password: String
) : Parcelable