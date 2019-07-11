package com.km.parceltracker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Parcel(
    val id: Long,
    val title: String,
    val sender: String?,
    val courier: String?,
    val trackingUrl: String?,
    val parcelStatus: String,
    val lastUpdated: Date
) : Parcelable