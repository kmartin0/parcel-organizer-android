package com.km.parceltracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Parcel(
    @PrimaryKey val id: Long,
    val title: String,
    val sender: String?,
    val courier: String?,
    val trackingUrl: String?,
    val parcelStatus: ParcelStatus,
    val lastUpdated: Date
) : Parcelable