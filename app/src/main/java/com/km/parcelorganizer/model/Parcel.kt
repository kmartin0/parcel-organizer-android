package com.km.parcelorganizer.model

import android.os.Parcelable
import android.webkit.URLUtil
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Parcel(
    var title: String,
    var sender: String?,
    var courier: String?,
    var trackingUrl: String?,
    var additionalInformation: String?,
    var parcelStatus: ParcelStatus,
    var lastUpdated: Date,
    var id: Long
) : Parcelable {
    fun parseTrackingUrl(): String? {
        return if (trackingUrl.isNullOrEmpty() || URLUtil.isValidUrl(trackingUrl)) trackingUrl
        else URLUtil.guessUrl(trackingUrl)
    }
}