package com.km.parceltracker.api.request

import com.km.parceltracker.model.ParcelStatus

data class RegisterParcelRequestBody(
    var title: String,
    var sender: String?,
    var courier: String?,
    var trackingUrl: String?,
    var parcelStatus: ParcelStatus
)