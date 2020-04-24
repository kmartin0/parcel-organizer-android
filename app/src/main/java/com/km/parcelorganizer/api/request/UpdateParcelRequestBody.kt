package com.km.parcelorganizer.api.request

import android.os.Parcelable
import com.km.parcelorganizer.model.ParcelStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateParcelRequestBody(
    var id: Long,
    var title: String,
    var sender: String?,
    var courier: String?,
    var trackingUrl: String?,
    var additionalInformation: String?,
    var parcelStatus: ParcelStatus
) : Parcelable