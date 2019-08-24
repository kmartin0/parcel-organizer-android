package com.km.parceltracker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Parcel(
    var title: String,
    var sender: String?,
    var courier: String?,
    var trackingUrl: String?,
    var parcelStatus: ParcelStatus,
    var lastUpdated: Date,
    var id: Long
) : Parcelable