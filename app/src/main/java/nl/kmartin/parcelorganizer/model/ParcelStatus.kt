package nl.kmartin.parcelorganizer.model

import android.os.Parcelable
import nl.kmartin.parcelorganizer.enums.ParcelStatusEnum
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParcelStatus(
    val id: Long,
    val status: ParcelStatusEnum
) : Parcelable