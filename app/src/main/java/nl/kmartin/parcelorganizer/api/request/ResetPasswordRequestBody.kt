package nl.kmartin.parcelorganizer.api.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResetPasswordRequestBody(
    val newPassword: String,
    val token: String
) : Parcelable