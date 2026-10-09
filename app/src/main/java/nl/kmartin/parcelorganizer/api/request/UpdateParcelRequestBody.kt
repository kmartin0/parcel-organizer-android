package nl.kmartin.parcelorganizer.api.request

import android.os.Parcelable
import nl.kmartin.parcelorganizer.model.ParcelStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateParcelRequestBody(
    var id: Long,
    var title: String,
    var sender: String?,
    var courier: String?,
    var trackingUrl: String?,
    var additionalInformation: String?,
    var parcelStatus: ParcelStatus
) : Parcelable