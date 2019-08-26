package com.km.parcelorganizer.model

import android.os.Parcelable
import com.km.parcelorganizer.enums.ParcelStatusEnum
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParcelStatus(
    val id: Long,
    val status: ParcelStatusEnum
) : Parcelable