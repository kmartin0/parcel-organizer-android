package com.km.parceltracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Parcel(
    var title: String,
    var sender: String?,
    var courier: String?,
    var trackingUrl: String?,
    var parcelStatus: ParcelStatus,
    var lastUpdated: Date,
    @PrimaryKey(autoGenerate = true) var id: Long
) : Parcelable