package com.km.parceltracker.api.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterRequestBody(
    val email: String,
    val name: String,
    val password: String
) : Parcelable