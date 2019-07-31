package com.km.parceltracker.model

import android.os.Parcelable
import com.km.parceltracker.enums.ParcelStatusEnum
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParcelStatus(
    val id: Long,
    val status: ParcelStatusEnum
) : Parcelable