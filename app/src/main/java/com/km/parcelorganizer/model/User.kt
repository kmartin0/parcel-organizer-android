package com.km.parcelorganizer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Long,
    val email: String,
    val name: String,
    val password: String,
    var OAuth2Credentials: OAuth2Credentials?
) : Parcelable