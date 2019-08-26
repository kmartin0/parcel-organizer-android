package com.km.parcelorganizer.api.request

import android.os.Parcelable
import com.km.parcelorganizer.model.ParcelStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterParcelRequestBody(
    var title: String,
    var sender: String?,
    var courier: String?,
    var trackingUrl: String?,
    var parcelStatus: ParcelStatus
) : Parcelable