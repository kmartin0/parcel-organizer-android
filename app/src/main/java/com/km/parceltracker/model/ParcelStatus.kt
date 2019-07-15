package com.km.parceltracker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParcelStatus(
    val id: Long,
    val status: String
) : Parcelable