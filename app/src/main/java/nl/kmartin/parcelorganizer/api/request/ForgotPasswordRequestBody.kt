package nl.kmartin.parcelorganizer.api.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForgotPasswordRequestBody(
    val email: String
) : Parcelable