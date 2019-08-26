package com.km.parceltracker.api.error

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TargetError(
    val target : String,
    val error : String
) : Parcelable