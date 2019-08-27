package com.km.parcelorganizer.api.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChangePasswordRequestBody(
    val currentPassword : String,
    val newPassword : String
) : Parcelable