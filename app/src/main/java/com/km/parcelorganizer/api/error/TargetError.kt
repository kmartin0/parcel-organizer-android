package com.km.parcelorganizer.api.error

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TargetError(
    val target: String,
    val error: String
) : Parcelable