package com.km.parceltracker.api.request

import android.os.Parcelable
import com.km.parceltracker.model.ParcelStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateParcelRequestBody(
    var id: Long,
    var title: String,
    var sender: String?,
    var courier: String?,
    var trackingUrl: String?,
    var parcelStatus: ParcelStatus
) : Parcelable